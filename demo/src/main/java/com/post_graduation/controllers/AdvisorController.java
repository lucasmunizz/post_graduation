package com.post_graduation.controllers;

import com.post_graduation.domain.advisor.Advisor;
import com.post_graduation.dto.advisor.AdvisorRequestDTO;
import com.post_graduation.dto.advisor.AdvisorResponseDTO;
import com.post_graduation.services.AdvisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/advisors")
public class AdvisorController {

    @Autowired
    private AdvisorService service;

    @GetMapping("/")
    public ResponseEntity<List<AdvisorResponseDTO>> findAll() {
        List<AdvisorResponseDTO> advisors = service.findAll();
        return ResponseEntity.ok().body(advisors);
    }

}
