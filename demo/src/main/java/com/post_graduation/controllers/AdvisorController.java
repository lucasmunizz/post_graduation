package com.post_graduation.controllers;

import com.post_graduation.domain.advisor.Advisor;
import com.post_graduation.dto.advisor.AdvisorHomeResponseDTO;
import com.post_graduation.dto.advisor.AdvisorRequestDTO;
import com.post_graduation.dto.advisor.AdvisorResponseDTO;
import com.post_graduation.dto.form.FormResponseDTO;
import com.post_graduation.dto.submission_period.SubmissionPeriodNewRequestDTO;
import com.post_graduation.dto.submission_period.SubmissionPeriodNewResponseDTO;
import com.post_graduation.services.AdvisorService;
import com.post_graduation.services.FormService;
import com.post_graduation.services.SubmissionPeriodService;
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

    @Autowired
    private SubmissionPeriodService submissionPeriodService;

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

    @PostMapping("/{id}/submission-period")
    public ResponseEntity<Void> createSubmissionPeriod(@PathVariable UUID id, @RequestBody SubmissionPeriodNewRequestDTO dto) {
         this.submissionPeriodService.createSubmissionPeriod(id, dto);
         return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}/submission-period")
    public List<SubmissionPeriodNewResponseDTO> findSubmissionPeriodsByAdvisor(@PathVariable UUID id) {
        return submissionPeriodService.findSubmissionPeriodsByAdvisor(id);
    }

}
