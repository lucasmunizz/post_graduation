package com.post_graduation.dto.submission_period;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record SubmissionPeriodNewRequestDTO (
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate startDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate endDate){
}
