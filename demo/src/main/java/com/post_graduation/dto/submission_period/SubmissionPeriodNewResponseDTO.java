package com.post_graduation.dto.submission_period;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.UUID;

public record SubmissionPeriodNewResponseDTO(
        UUID submissionPeriodId,

        UUID advisorId,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate startDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate endDate) {
}
