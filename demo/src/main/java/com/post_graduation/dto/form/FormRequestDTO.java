package com.post_graduation.dto.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.post_graduation.domain.advisor.Advisor;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import java.time.LocalDate;
import java.util.UUID;

public record FormRequestDTO(

        String studentEmail,
        String studentName,

 String advisorEmail,

 String uspNumber,

 String lattesLink,

@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
 LocalDate lattesUpdateDate,

 String discipline,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate entryDate,

 String lastReportResult,

 String approvalsFromTheBegginigOfTheCourse,

 String repprovalsOnSecondSemester,

 String repprovalsFromTheBegginigOfTheCourse,

 String proficiencyExam,

 String qualifyingExam,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
LocalDate maximumRegistrationDeadline,

@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate deadlineDissertation,

 String articlesWritingPhase,

 String articlesInEvaluation,

 String acceptedArticles,

 String activities,

 String researchActivitiesResume,

 String additionalComments,

 String hasDifficulty) {
}
