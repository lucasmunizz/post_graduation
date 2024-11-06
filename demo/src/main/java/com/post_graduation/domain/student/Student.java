package com.post_graduation.domain.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.post_graduation.domain.subject.Subject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import com.post_graduation.domain.advisor.Advisor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String uspNumber;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    private String RG;

    private String birthSpot;

    private String nacionality;

    private String discipline;

    @OneToOne
    @JoinColumn(name = "advisor_id")
    private Advisor advisor_id;

    private String lattesLink;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate registrationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate qualifyingExamDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate proficiencyExamDate;

    @ManyToMany
    @JoinTable(
            name = "student_approved_subjects",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject>approvedsSubjects;

    @ManyToMany
    @JoinTable(
            name = "student_repproved_subjects",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject>repprovedSubjects;
}
