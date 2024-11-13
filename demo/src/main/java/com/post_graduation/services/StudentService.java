package com.post_graduation.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.post_graduation.domain.advisor.Advisor;
import com.post_graduation.domain.student.Student;
import com.post_graduation.domain.subject.Subject;
import com.post_graduation.dto.advisor.AdvisorHomeResponseDTO;
import com.post_graduation.dto.auth.LoginRequestDTO;
import com.post_graduation.dto.auth.LoginResponseDTO;
import com.post_graduation.dto.student.LoginStudentRequestDTO;
import com.post_graduation.dto.student.StudentHomeResponseDTO;
import com.post_graduation.dto.student.StudentRequestDTO;
import com.post_graduation.dto.student.StudentResponseDTO;
import com.post_graduation.repositories.AdvisorRepository;
import com.post_graduation.repositories.StudentRepository;
import com.post_graduation.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.security.sasl.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.time.Duration;
import java.time.Instant;
import java.util.*;


import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Value("${api.security.token.secret}")
    private String secretKey;

    @Autowired
    private PasswordEncoder encoder;


    @Autowired
    private PasswordEncoder passwordEncoder;
    public LoginStudentRequestDTO create(StudentRequestDTO studentDTO){

        Optional<Student> user = this.repository.findByEmail(studentDTO.email());

        if (user.isPresent()){

            throw new RuntimeException("Student already registered");

        }

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
        student.setDeadlineDissertation(studentDTO.deadlineDissertation());

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
        var roles = Arrays.asList("STUDENT");


        this.repository.save(student);

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var expiresIn = Instant.now().plus(Duration.ofMinutes(120));
        var token = JWT.create()
                .withIssuer("javagas")
                .withSubject(student.getId().toString())
                .withClaim("roles", roles)
                .withExpiresAt(expiresIn)
                .sign(algorithm);


        return new LoginStudentRequestDTO(token, roles);

    }

    public List<StudentResponseDTO> findAllStudents() {
        return this.repository.findAll().stream()
                .map(StudentResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public LoginStudentRequestDTO login(LoginRequestDTO loginRequestDTO) throws AuthenticationException{
        var candidate = this.repository.findByEmail(loginRequestDTO.email())
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("Username/password incorrect");
                });

        var passwordMatches = this.passwordEncoder
                .matches(loginRequestDTO.password(), candidate.getPassword());

        if (!passwordMatches) {
            throw new AuthenticationException();
        }

        var roles = Arrays.asList("STUDENT");

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var expiresIn = Instant.now().plus(Duration.ofMinutes(10));
        var token = JWT.create()
                .withIssuer("javagas")
                .withSubject(candidate.getId().toString())
                .withClaim("roles", roles)
                .withExpiresAt(expiresIn)
                .sign(algorithm);

        var AuthCandidateResponse = new LoginStudentRequestDTO(token, roles);

        return AuthCandidateResponse;
    }

    public StudentHomeResponseDTO getStudentById(UUID studentId){
        Student student = this.repository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));

        StudentHomeResponseDTO studentHomeResponseDTO = new StudentHomeResponseDTO(student.getFirstName() + " " + student.getLastName(), student.getEmail());

        return studentHomeResponseDTO;
    }

}
