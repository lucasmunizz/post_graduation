package com.post_graduation.controllers;

import com.post_graduation.dto.advisor.AdvisorRequestDTO;
import com.post_graduation.dto.auth.LoginRequestDTO;
import com.post_graduation.dto.student.StudentRequestDTO;
import com.post_graduation.services.AdvisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/advisors")
public class AuthAdvisorController {

    @Autowired
    AdvisorService advisorService;

    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDTO authCandidateRequestDTO) {
        try {
            var token = this.advisorService.login(authCandidateRequestDTO);

            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody AdvisorRequestDTO advisorRequestDTO){

        try {
            var token = this.advisorService.create(advisorRequestDTO);

            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
