package com.post_graduation.dto.advisor;


import com.post_graduation.dto.student.StudentResponseDTO;

import java.util.List;
import java.util.UUID;

public record AdvisorResponseDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        List<StudentResponseDTO> students
) {}