package com.post_graduation.controllers;

import com.post_graduation.dto.advisor.AdvisorHomeResponseDTO;
import com.post_graduation.dto.advisor.AdvisorResponseDTO;
import com.post_graduation.dto.form.FormEvalCCPDTO;
import com.post_graduation.dto.form.FormEvalDTO;
import com.post_graduation.dto.form.FormRequestDTO;
import com.post_graduation.dto.form.FormResponseDTO;
import com.post_graduation.services.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PutMapping("/{formId}")
    public ResponseEntity<Object> updateForm(@PathVariable UUID formId, @RequestBody FormRequestDTO formRequest) {
        try {
            this.formService.updateForm(formId, formRequest);
            return ResponseEntity.ok("Form updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/advisor-note")
    public ResponseEntity<Void> updateAdvisorNote(@PathVariable UUID id, @RequestBody FormEvalDTO dto) {
        formService.updateAdvisorNote(id, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/ccp-opinion")
    public ResponseEntity<Void> updateCcpOpinion(@PathVariable UUID id, @RequestBody FormEvalCCPDTO dto) {
        formService.updateCcpOpinion(id, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormResponseDTO> getForm(@PathVariable UUID id){
        FormResponseDTO formResponseDTO = this.formService.findFormById(id);
        return ResponseEntity.ok(formResponseDTO);
    }

    @GetMapping("/evaluated-forms")
    public ResponseEntity<List<FormResponseDTO>> findAll() {
        List<FormResponseDTO> forms = this.formService.findEvaluatedForms();
        return ResponseEntity.ok().body(forms);
    }
}