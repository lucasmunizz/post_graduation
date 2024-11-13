package com.post_graduation.controllers;

import com.post_graduation.dto.advisor.AdvisorHomeResponseDTO;
import com.post_graduation.dto.form.FormEvalDTO;
import com.post_graduation.dto.form.FormRequestDTO;
import com.post_graduation.dto.form.FormResponseDTO;
import com.post_graduation.services.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/forms")
public class FormController {

    @Autowired
    private FormService formService;

    @PostMapping("/")
    public ResponseEntity<Void> createForm(@RequestBody FormRequestDTO dto) {
        formService.create(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}/advisor-note")
    public ResponseEntity<Void> updateAdvisorNote(@PathVariable UUID id, @RequestBody FormEvalDTO dto) {
        formService.updateAdvisorNote(id, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/{id}")
    public ResponseEntity<FormResponseDTO> getForm(@PathVariable UUID id){
        FormResponseDTO formResponseDTO = this.formService.findFormById(id);
        return ResponseEntity.ok(formResponseDTO);
    }
}