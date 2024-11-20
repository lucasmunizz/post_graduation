package com.post_graduation.controllers;

import com.post_graduation.domain.submission_period.SubmissionPeriod;
import com.post_graduation.dto.advisor.AdvisorRequestDTO;
import com.post_graduation.dto.advisor.AdvisorResponseDTO;
import com.post_graduation.dto.auth.LoginRequestDTO;
import com.post_graduation.dto.ccp.CCPLoginRequestDTO;
import com.post_graduation.dto.submission_period.SubmissionPeriodRequestDTO;
import com.post_graduation.services.CCPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ccp")
public class CCPController {

    @Autowired
    private CCPService service;


    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody CCPLoginRequestDTO ccpLoginRequestDTO){

        try {
            var token = this.service.create(ccpLoginRequestDTO);

            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody CCPLoginRequestDTO ccpLoginRequestDTO) {
        try {
            var token = this.service.login(ccpLoginRequestDTO);

            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/general-opinion")
    public ResponseEntity<Void> giveOpinion() {
        this.service.generalOpinion();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
