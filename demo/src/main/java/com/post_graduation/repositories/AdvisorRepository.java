package com.post_graduation.repositories;

import com.post_graduation.domain.advisor.Advisor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AdvisorRepository extends JpaRepository<Advisor, UUID> {

    public Optional<Advisor> findByEmail(String email);
}
