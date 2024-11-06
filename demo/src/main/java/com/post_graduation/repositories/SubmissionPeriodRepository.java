package com.post_graduation.repositories;

import com.post_graduation.domain.submission_period.SubmissionPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubmissionPeriodRepository extends JpaRepository<SubmissionPeriod, UUID> {
}
