package com.post_graduation.services;

import com.post_graduation.domain.advisor.Advisor;
import com.post_graduation.domain.submission_period.SubmissionPeriod;
import com.post_graduation.dto.student.StudentResponseDTO;
import com.post_graduation.dto.submission_period.SubmissionPeriodRequestDTO;
import com.post_graduation.dto.submission_period.SubmissionPeriodResponseDTO;
import com.post_graduation.repositories.AdvisorRepository;
import com.post_graduation.repositories.SubmissionPeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubmissionPeriodService {

    @Autowired
    private SubmissionPeriodRepository repository;

    @Autowired
    private AdvisorRepository advisorRepository;

    public SubmissionPeriod create(SubmissionPeriodRequestDTO dto){
        Advisor advisor = this.advisorRepository.findById(dto.advisorId())
                .orElseThrow(() -> new RuntimeException("Advisor not found with email: " + dto.advisorId()));

        SubmissionPeriod submissionPeriod = new SubmissionPeriod();

        submissionPeriod.setStartDate(dto.startDate());
        submissionPeriod.setEndDate(dto.endDate());
        submissionPeriod.setAdvisor_id(advisor);

        return this.repository.save(submissionPeriod);
    }

    public List<SubmissionPeriodResponseDTO> findAll() {
        return this.repository.findAll().stream()
                .map(SubmissionPeriodResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
