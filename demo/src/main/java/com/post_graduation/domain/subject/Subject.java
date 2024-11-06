package com.post_graduation.domain.subject;

import com.post_graduation.domain.student.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;

    // Relacionamento bidirecional com estudantes que foram aprovados na disciplina
    @ManyToMany(mappedBy = "approvedsSubjects")
    private Set<Student> studentsApproved;

    // Relacionamento bidirecional com estudantes que foram reprovados na disciplina
    @ManyToMany(mappedBy = "repprovedSubjects")
    private Set<Student> studentsRepproved;
}
