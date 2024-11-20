package com.post_graduation.dto.student;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record StudentHomeResponseDTO(
        String name,
        String email,

        String advisorEmail,

        String uspNumber,

        String discipline,

        String lattesLink,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate deadlineDissertation


        ) {
}
