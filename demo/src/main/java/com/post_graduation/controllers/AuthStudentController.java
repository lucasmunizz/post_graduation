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
import com.post_graduation.services.StudentService;
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
@RequestMapping("/students")
public class AuthStudentController {

    @Autowired
    private StudentService studentService;


    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDTO authCandidateRequestDTO) {
        try {
            var token = this.studentService.login(authCandidateRequestDTO);

            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody StudentRequestDTO studentDTO){

        try {
            var token = this.studentService.create(studentDTO);

            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


}
