package com.post_graduation.controllers;

import com.post_graduation.dto.form.FormRequestDTO;
import com.post_graduation.services.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}