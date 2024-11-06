package com.post_graduation.dto.student;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record StudentRequestDTO(

        String uspNumber,
        String firstName,
         String lastName,
        String email,
        String password,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate birthDate,

         String RG,
        String birthSpot,
        String nacionality,
        String discipline,
        String advisorEmail, // ID do orientador
        String lattesLink,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate registrationDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate qualifyingExamDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate proficiencyExamDate,
        Set<UUID> approvedsSubjectIds,
        Set<UUID> repprovedSubjectIds


) {
}
