package com.post_graduation.dto.submission_period;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.post_graduation.domain.student.Student;
import com.post_graduation.domain.submission_period.SubmissionPeriod;
import com.post_graduation.dto.student.StudentResponseDTO;

import java.time.LocalDate;
import java.util.UUID;

public record SubmissionPeriodResponseDTO(
        String advisorEmail,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate startDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate endDate
) {

    public static SubmissionPeriodResponseDTO fromEntity(SubmissionPeriod submissionPeriod) {
        return new SubmissionPeriodResponseDTO(
                submissionPeriod.getAdvisor_id() != null ? submissionPeriod.getAdvisor_id().getEmail() : null, // Obtenção do advisorEmail,
                submissionPeriod.getStartDate(),
                submissionPeriod.getEndDate()
        );
    }
}



