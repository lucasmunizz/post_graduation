package com.post_graduation.controllers;

import com.post_graduation.domain.subject.Subject;
import com.post_graduation.dto.subject.SubjectIdDTO;
import com.post_graduation.dto.subject.SubjectRequestDTO;
import com.post_graduation.dto.subject.SubjectResponseDTO;
import com.post_graduation.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/")
    public  ResponseEntity<List<SubjectResponseDTO>> findAll(){
        List<SubjectResponseDTO> subjects = this.subjectService.findAll();
        return ResponseEntity.ok(subjects);
    }

    @PostMapping("/")
    public ResponseEntity<SubjectIdDTO> create(@RequestBody SubjectRequestDTO requestDTO, UriComponentsBuilder uriComponentsBuilder){

        SubjectIdDTO subject = this.subjectService.create(requestDTO);

        var uri = uriComponentsBuilder.path("/subjects/{id}").buildAndExpand(subject.id()).toUri();
        return ResponseEntity.created(uri).body(subject);

    }
}
