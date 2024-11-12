package com.post_graduation.controllers;

import com.post_graduation.domain.advisor.Advisor;
import com.post_graduation.domain.student.Student;
import com.post_graduation.domain.subject.Subject;
import com.post_graduation.dto.auth.LoginRequestDTO;
import com.post_graduation.dto.auth.LoginResponseDTO;
import com.post_graduation.dto.student.StudentRequestDTO;
import com.post_graduation.infra.security.TokenService;
import com.post_graduation.repositories.AdvisorRepository;
import com.post_graduation.repositories.StudentRepository;
import com.post_graduation.repositories.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    @Autowired
    private StudentRepository repository;
    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login/advisor")
    public ResponseEntity<LoginResponseDTO> loginAdvisor(@RequestBody LoginRequestDTO body){
        Advisor advisor = this.advisorRepository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if (2 == 2){
            String token = this.tokenService.generateAdvisorToken(advisor);
            return ResponseEntity.ok(new LoginResponseDTO(advisor.getFirstName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/login/student")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO body){
        Student user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if (encoder.matches(body.password(), user.getPassword())){
            String token = this.tokenService.generateStudentToken(user);
            return ResponseEntity.ok(new LoginResponseDTO(user.getFirstName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody StudentRequestDTO studentDTO){


        Optional<Student> user = this.repository.findByEmail(studentDTO.email());

        if (user.isEmpty()){
            this.repository.findByEmail(studentDTO.email())
                    .ifPresent(existingStudent -> {
                        throw new IllegalArgumentException("Email já está em uso: " + studentDTO.email());
                    });

            Advisor advisor = this.advisorRepository.findByEmail(studentDTO.advisorEmail())
                    .orElseThrow(() -> new RuntimeException("Advisor not found with email: " + studentDTO.advisorEmail()));


            Student student = new Student();
            student.setUspNumber(studentDTO.uspNumber());
            student.setFirstName(studentDTO.firstName());
            student.setLastName(studentDTO.lastName());
            student.setEmail(studentDTO.email());
            student.setPassword(encoder.encode(studentDTO.password()));
            student.setBirthDate(studentDTO.birthDate());
            student.setRG(studentDTO.RG());
            student.setBirthSpot(studentDTO.birthSpot());
            student.setNacionality(studentDTO.nacionality());
            student.setDiscipline(studentDTO.discipline());
            student.setLattesLink(studentDTO.lattesLink());
            student.setRegistrationDate(studentDTO.registrationDate());
            student.setQualifyingExamDate(studentDTO.qualifyingExamDate());
            student.setProficiencyExamDate(studentDTO.proficiencyExamDate());
            student.setAdvisor_id(advisor);

            // Busca as matérias aprovadas e reprovadas com base nos IDs fornecidos
            Set<Subject> approvedSubjects = studentDTO.approvedsSubjectIds().stream()
                    .map(subjectRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            student.setApprovedsSubjects(approvedSubjects);

            Set<Subject> repprovedSubjects = studentDTO.repprovedSubjectIds().stream()
                    .map(subjectRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            student.setRepprovedSubjects(repprovedSubjects);
            String token = this.tokenService.generateStudentToken(student);

            this.repository.save(student);

            return ResponseEntity.ok(new LoginResponseDTO(student.getFirstName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

}
