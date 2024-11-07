package com.post_graduation.repositories;

import com.post_graduation.domain.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    public Optional<Student> findByEmail(String email);

    public Optional<Student> findByUspNumber(String uspNumber);
}
