package com.post_graduation.services;

import com.post_graduation.domain.advisor.Advisor;
import com.post_graduation.domain.form.Form;
import com.post_graduation.domain.submission_period.SubmissionPeriod;
import com.post_graduation.dto.form.FormResponseDTO;
import com.post_graduation.dto.student.StudentResponseDTO;
import com.post_graduation.dto.submission_period.SubmissionPeriodNewRequestDTO;
import com.post_graduation.dto.submission_period.SubmissionPeriodNewResponseDTO;
import com.post_graduation.dto.submission_period.SubmissionPeriodRequestDTO;
import com.post_graduation.dto.submission_period.SubmissionPeriodResponseDTO;
import com.post_graduation.repositories.AdvisorRepository;
import com.post_graduation.repositories.SubmissionPeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubmissionPeriodService {

    @Autowired
    private SubmissionPeriodRepository repository;

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private MailService mailService;

    public SubmissionPeriod create(SubmissionPeriodRequestDTO dto){
        Advisor advisor = this.advisorRepository.findById(dto.advisorId())
                .orElseThrow(() -> new RuntimeException("Advisor not found with email: " + dto.advisorId()));

        List<String> recipients = new ArrayList<>();
        advisor.getStudents().forEach(student -> recipients.add(student.getEmail()));

        SubmissionPeriod submissionPeriod = new SubmissionPeriod();

        submissionPeriod.setStartDate(dto.startDate());
        submissionPeriod.setEndDate(dto.endDate());
        submissionPeriod.setAdvisor(advisor);

        this.mailService.sendEmailWithManyRecipients(recipients, "Data limite de submisão", "Atenção a data limite de submissão do seu relatório é: " + submissionPeriod.getEndDate());

        return this.repository.save(submissionPeriod);
    }

    public SubmissionPeriod createSubmissionPeriod(UUID advisorId, SubmissionPeriodNewRequestDTO dto){
        Advisor advisor = this.advisorRepository.findById(advisorId)
                .orElseThrow(() -> new RuntimeException("Advisor not found with email: " + advisorId));

        List<String> recipients = new ArrayList<>();
        advisor.getStudents().forEach(student -> recipients.add(student.getEmail()));

        SubmissionPeriod submissionPeriod = new SubmissionPeriod();

        submissionPeriod.setStartDate(dto.startDate());
        submissionPeriod.setEndDate(dto.endDate());
        submissionPeriod.setAdvisor(advisor);

        this.mailService.sendEmailWithManyRecipients(recipients, "Data limite de submisão", "Atenção a data limite de submissão do seu relatório é: " + submissionPeriod.getEndDate());

        return this.repository.save(submissionPeriod);
    }

    public List<SubmissionPeriodResponseDTO> findAll() {
        return this.repository.findAll().stream()
                .map(SubmissionPeriodResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<SubmissionPeriodNewResponseDTO> findSubmissionPeriodsByAdvisor(UUID advisorId) {
        List<SubmissionPeriod> submissionPeriods = repository.findByAdvisor_id(advisorId);
        return submissionPeriods.stream()
                .map(period -> new SubmissionPeriodNewResponseDTO(
                        period.getId(),
                        period.getAdvisor().getId(),
                        period.getStartDate(),
                        period.getEndDate()
                ))
                .collect(Collectors.toList());

    }
}
