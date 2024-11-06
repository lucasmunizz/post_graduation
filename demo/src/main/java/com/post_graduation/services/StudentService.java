package com.post_graduation.services;

import com.post_graduation.domain.advisor.Advisor;
import com.post_graduation.domain.student.Student;
import com.post_graduation.domain.subject.Subject;
import com.post_graduation.dto.student.StudentRequestDTO;
import com.post_graduation.dto.student.StudentResponseDTO;
import com.post_graduation.repositories.AdvisorRepository;
import com.post_graduation.repositories.StudentRepository;
import com.post_graduation.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private SubjectRepository subjectRepository;
    public Student create(StudentRequestDTO studentDTO){

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
        student.setPassword(studentDTO.password());
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

        return repository.save(student);
    }

    public List<StudentResponseDTO> findAllStudents() {
        return this.repository.findAll().stream()
                .map(StudentResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
