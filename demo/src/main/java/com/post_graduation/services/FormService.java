package com.post_graduation.services;

import com.post_graduation.domain.form.Form;
import com.post_graduation.domain.advisor.Advisor;
import com.post_graduation.dto.form.FormRequestDTO;
import com.post_graduation.repositories.AdvisorRepository;
import com.post_graduation.repositories.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class FormService {

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private MailService mailService;

    @Transactional
    public Form create(FormRequestDTO dto) {
        // Busca o advisor pelo ID fornecido
        Advisor advisor = advisorRepository.findById(dto.advisor_id())
                .orElseThrow(() -> new IllegalArgumentException("Advisor not found"));

        // Cria uma nova instância de Form preenchendo os dados do DTO
        Form form = new Form();
        form.setStudentName(dto.studentName());
        form.setAdvisor_id(advisor);
        form.setUspNumber(dto.uspNumber());
        form.setLattesLink(dto.lattesLink());
        form.setLattesUpdateDate(dto.lattesUpdateDate());
        form.setDiscipline(dto.discipline());
        form.setLastReportResult(dto.lastReportResult());
        form.setApprovalsFromTheBegginigOfTheCourse(dto.approvalsFromTheBegginigOfTheCourse());
        form.setRepprovalsOnSecondSemester(dto.repprovalsOnSecondSemester());
        form.setRepprovalsFromTheBegginigOfTheCourse(dto.repprovalsFromTheBegginigOfTheCourse());
        form.setProficiencyExam(dto.proficiencyExam());
        form.setQualifyingExam(dto.qualifyingExam());
        form.setDeadlineDissertation(dto.deadlineDissertation());
        form.setArticlesWritingPhase(dto.articlesWritingPhase());
        form.setArticlesInEvaluation(dto.articlesInEvaluation());
        form.setAcceptedArticles(dto.acceptedArticles());
        form.setActivities(dto.activities());
        form.setResearchActivitiesResume(dto.researchActivitiesResume());
        form.setAdditionalComments(dto.additionalComments());
        form.setHasDifficulty(dto.hasDifficulty());
        form.setSubmissionDate(LocalDate.now());

        String message = "O aluno " + dto.studentName() + ", de número USP: " + dto.uspNumber() + " enviou um relatório para aprovação, acesse já o sistema.";

        this.mailService.sendEmail(advisor.getEmail(), "Relatório pendente de avaliação", message);

        // Salva a entidade Form no banco de dados
        return formRepository.save(form);
    }
}
