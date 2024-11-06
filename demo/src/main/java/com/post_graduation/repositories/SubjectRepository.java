package com.post_graduation.repositories;

import com.post_graduation.domain.subject.Subject;
import com.post_graduation.dto.subject.SubjectRequestDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, UUID> {

    public Optional<Subject> findByName(SubjectRequestDTO dto);
}
