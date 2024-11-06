package com.post_graduation.dto.subject;

import com.post_graduation.dto.student.StudentResponseDTO;

import java.util.List;
import java.util.UUID;

public record SubjectResponseDTO(
        UUID id,
        String name,
        List<StudentResponseDTO> studentsApproved,
        List<StudentResponseDTO> studentsRepproved) {

}
