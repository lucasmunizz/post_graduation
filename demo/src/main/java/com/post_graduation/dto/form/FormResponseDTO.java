package com.post_graduation.dto.form;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.UUID;

public record FormResponseDTO(

        UUID formId,

         String studentEmail,
         String studentName,

        String advisorEmail,
        String uspNumber,
        String lattesLink,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
         LocalDate lattesUpdateDate,

        String discipline,
        String lastReportResult,
        String approvalsFromTheBegginigOfTheCourse,
        String repprovalsOnSecondSemester,
        String repprovalsFromTheBegginigOfTheCourse,
        String proficiencyExam,
        String qualifyingExam,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
 LocalDate deadlineDissertation,

        String articlesWritingPhase,
        String articlesInEvaluation,
        String acceptedArticles,
        String activities,
        String researchActivitiesResume,
        String additionalComments,
        String hasDifficulty,
        Integer version,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
 LocalDate submissionDate,

        String advisorNote,
        String ccpOpinion,
        String statusEvaluation) {
}
