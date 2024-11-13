package com.post_graduation.controllers;

import com.post_graduation.domain.advisor.Advisor;
import com.post_graduation.dto.advisor.AdvisorHomeResponseDTO;
import com.post_graduation.dto.advisor.AdvisorRequestDTO;
import com.post_graduation.dto.advisor.AdvisorResponseDTO;
import com.post_graduation.dto.form.FormResponseDTO;
import com.post_graduation.services.AdvisorService;
import com.post_graduation.services.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/advisors")
public class AdvisorController {

    @Autowired
    private AdvisorService service;

    @Autowired
    private FormService formService;

    @GetMapping("/")
    public ResponseEntity<List<AdvisorResponseDTO>> findAll() {
        List<AdvisorResponseDTO> advisors = service.findAll();
        return ResponseEntity.ok().body(advisors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvisorHomeResponseDTO> getAdvisor(@PathVariable UUID id){
        AdvisorHomeResponseDTO advisorHomeResponseDTO = this.service.getAdvisorById(id);
        return ResponseEntity.ok(advisorHomeResponseDTO);
    }

    @GetMapping("/{id}/forms")
    public List<FormResponseDTO> findFormsByAdvisor(@PathVariable UUID id) {
        return formService.findFormsByAdvisor(id);
    }

}
