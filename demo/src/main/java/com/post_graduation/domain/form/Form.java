package com.post_graduation.domain.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.post_graduation.domain.advisor.Advisor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "forms")
@Getter
@Setter
public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String studentName;

    @OneToOne
    @JoinColumn(name = "advisor_id")
    private Advisor advisor_id;

    private String uspNumber;

    private String lattesLink;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate lattesUpdateDate;

    private String discipline;

    private String lastReportResult;

    private String approvalsFromTheBegginigOfTheCourse;

    private String repprovalsOnSecondSemester;

    private String repprovalsFromTheBegginigOfTheCourse;

    private String proficiencyExam;

    private String qualifyingExam;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate deadlineDissertation;

    private String articlesWritingPhase;

    private String articlesInEvaluation;

    private String acceptedArticles;

    private String activities;

    private String researchActivitiesResume;

    private String additionalComments;

    private String hasDifficulty;
}
