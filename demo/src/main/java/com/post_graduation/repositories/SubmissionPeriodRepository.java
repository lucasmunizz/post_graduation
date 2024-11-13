package com.post_graduation.repositories;

import com.post_graduation.domain.form.Form;
import com.post_graduation.domain.submission_period.SubmissionPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubmissionPeriodRepository extends JpaRepository<SubmissionPeriod, UUID> {

    List<SubmissionPeriod> findByAdvisor_id(UUID advisorId);
}
