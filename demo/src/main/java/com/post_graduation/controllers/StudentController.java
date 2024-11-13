package com.post_graduation.controllers;

import com.post_graduation.domain.student.Student;
import com.post_graduation.dto.student.StudentRequestDTO;
import com.post_graduation.dto.student.StudentResponseDTO;
import com.post_graduation.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;


    @GetMapping("/")
    public ResponseEntity<List<StudentResponseDTO>> findAll() {
        List<StudentResponseDTO> students = studentService.findAllStudents();
        return ResponseEntity.ok(students);
    }
}