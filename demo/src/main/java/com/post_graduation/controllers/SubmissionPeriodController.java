package com.post_graduation.controllers;

import com.post_graduation.domain.student.Student;
import com.post_graduation.domain.submission_period.SubmissionPeriod;
import com.post_graduation.dto.student.StudentRequestDTO;
import com.post_graduation.dto.student.StudentResponseDTO;
import com.post_graduation.dto.submission_period.SubmissionPeriodRequestDTO;
import com.post_graduation.dto.submission_period.SubmissionPeriodResponseDTO;
import com.post_graduation.services.SubmissionPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submission-period")
public class SubmissionPeriodController {


    @Autowired
    private SubmissionPeriodService service;

    @PostMapping("/")
    public ResponseEntity<Void> create(@RequestBody SubmissionPeriodRequestDTO dto) {
        SubmissionPeriod createdPeriod = this.service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/")
    public ResponseEntity<List<SubmissionPeriodResponseDTO>> findAll() {
        List<SubmissionPeriodResponseDTO> submission = service.findAll();
        return ResponseEntity.ok(submission);
    }
}
