package com.post_graduation.dto.student;

import com.post_graduation.domain.student.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record StudentResponseDTO(
        UUID id,
        String uspNumber,
        String firstName,
        String lastName,
        String email,
        LocalDate birthDate,
        String birthSpot,
        String nacionality,
        String discipline,
        String advisorEmail // Mudança aqui
) {

    public static StudentResponseDTO fromEntity(Student student) {
        return new StudentResponseDTO(
                student.getId(),
                student.getUspNumber(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getBirthDate(),
                student.getBirthSpot(),
                student.getNacionality(),
                student.getDiscipline(),
                student.getAdvisor_id() != null ? student.getAdvisor_id().getEmail() : null // Obtenção do advisorEmail
        );
    }
}
