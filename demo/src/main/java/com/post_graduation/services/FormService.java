package com.post_graduation.services;

import com.post_graduation.domain.form.Form;
import com.post_graduation.domain.advisor.Advisor;
import com.post_graduation.domain.student.Student;
import com.post_graduation.dto.form.FormEvalCCPDTO;
import com.post_graduation.dto.form.FormEvalDTO;
import com.post_graduation.dto.form.FormRequestDTO;
import com.post_graduation.dto.form.FormResponseDTO;
import com.post_graduation.repositories.AdvisorRepository;
import com.post_graduation.repositories.FormRepository;
import com.post_graduation.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FormService {

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MailService mailService;

    @Transactional
    public Form create(FormRequestDTO dto) {
        // Busca o advisor pelo ID fornecido
        Advisor advisor = advisorRepository.findByEmail(dto.advisorEmail())
                .orElseThrow(() -> new IllegalArgumentException("Advisor not found"));

        Student student = studentRepository.findByUspNumber(dto.uspNumber())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));


        // Cria uma nova instância de Form preenchendo os dados do DTO
        Form form = new Form();
        form.setStudentEmail(dto.studentEmail());
        form.setStudentName(dto.studentName());
        form.setAdvisor(advisor);
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
        form.setStudent(student);
        form.setEntryDate(dto.entryTime());
        form.setMaximumRegistrationDeadline(dto.maximumRegistrationDeadline());

        String message = "O aluno " + dto.studentName() + ", de número USP: " + dto.uspNumber() + " enviou um relatório para aprovação, acesse já o sistema.";

        this.mailService.sendEmail(advisor.getEmail(), "Relatório pendente de avaliação", message);

        // Salva a entidade Form no banco de dados
        return formRepository.save(form);
    }

    public Form updateCcpOpinion(UUID formId, FormEvalCCPDTO dto) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Form not found"));

        form.setCcpOpinion(dto.ccpOpinion());

        Student student = studentRepository.findByUspNumber(form.getUspNumber())
                .orElseThrow(() -> new RuntimeException("Student not found"));


        String message = "Caro aluno(a) " + form.getStudentName() + ", seu relatório acaba de receber um parecer da Comissão de Pós Graduação:\n" + "Sua avaliação ficou como: " + dto.ccpOpinion() + "\nPara mais detalhes, acesse o sistema.";

        this.mailService.sendEmail(student.getEmail(), "Relatório avaliado", message);

        return formRepository.save(form);
    }

    public Form updateAdvisorNote(UUID formId, FormEvalDTO dto) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Form not found"));

        form.setAdvisorNote(dto.advisorNote());

        Student student = studentRepository.findByUspNumber(form.getUspNumber())
                .orElseThrow(() -> new RuntimeException("Student not found"));


        String message = "Caro aluno(a) " + form.getStudentName() + ", seu relatório acaba de receber uma avalição de seu orientador e já está disponível no sistema.";

        this.mailService.sendEmail(student.getEmail(), "Relatório avaliado", message);

        return formRepository.save(form);
    }

    public FormResponseDTO findFormById(UUID formId){

        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Form not found"));



        return new FormResponseDTO(
                form.getId(),
                form.getStudentEmail(),
                form.getStudentName(),
                form.getAdvisor().getEmail(),
                form.getUspNumber(),
                form.getLattesLink(),
                form.getLattesUpdateDate(),
                form.getDiscipline(),
                form.getEntryDate(),
                form.getLastReportResult(),
                form.getApprovalsFromTheBegginigOfTheCourse(),
                form.getRepprovalsOnSecondSemester(),
                form.getRepprovalsFromTheBegginigOfTheCourse(),
                form.getProficiencyExam(),
                form.getQualifyingExam(),
                form.getMaximumRegistrationDeadline(),
                form.getDeadlineDissertation(),
                form.getArticlesWritingPhase(),
                form.getArticlesInEvaluation(),
                form.getAcceptedArticles(),
                form.getActivities(),
                form.getResearchActivitiesResume(),
                form.getAdditionalComments(),
                form.getHasDifficulty(),
                form.getVersion(),
                form.getSubmissionDate(),
                form.getAdvisorNote(),
                form.getCcpOpinion(),
                form.getStatusEvaluation());
    }

    public List<FormResponseDTO> findEvaluatedForms() {
        List<Form> forms = formRepository.findByAdvisorNoteIsNotNull();
        return forms.stream()
                .map(form -> new FormResponseDTO(
                        form.getId(),
                        form.getStudentEmail(),
                        form.getStudentName(),
                        form.getAdvisor().getEmail(),
                        form.getUspNumber(),
                        form.getLattesLink(),
                        form.getLattesUpdateDate(),
                        form.getDiscipline(),
                        form.getEntryDate(),
                        form.getLastReportResult(),
                        form.getApprovalsFromTheBegginigOfTheCourse(),
                        form.getRepprovalsOnSecondSemester(),
                        form.getRepprovalsFromTheBegginigOfTheCourse(),
                        form.getProficiencyExam(),
                        form.getQualifyingExam(),
                        form.getMaximumRegistrationDeadline(),
                        form.getDeadlineDissertation(),
                        form.getArticlesWritingPhase(),
                        form.getArticlesInEvaluation(),
                        form.getAcceptedArticles(),
                        form.getActivities(),
                        form.getResearchActivitiesResume(),
                        form.getAdditionalComments(),
                        form.getHasDifficulty(),
                        form.getVersion(),
                        form.getSubmissionDate(),
                        form.getAdvisorNote(),
                        form.getCcpOpinion(),
                        form.getStatusEvaluation()
                ))
                .collect(Collectors.toList());
    }

    public List<FormResponseDTO> findFormsByAdvisor(UUID advisorId) {
        List<Form> forms = formRepository.findByAdvisor_id(advisorId);
        return forms.stream()
                .map(form -> new FormResponseDTO(
                        form.getId(),
                        form.getStudentEmail(),
                        form.getStudentName(),
                        form.getAdvisor().getEmail(),
                        form.getUspNumber(),
                        form.getLattesLink(),
                        form.getLattesUpdateDate(),
                        form.getDiscipline(),
                        form.getEntryDate(),
                        form.getLastReportResult(),
                        form.getApprovalsFromTheBegginigOfTheCourse(),
                        form.getRepprovalsOnSecondSemester(),
                        form.getRepprovalsFromTheBegginigOfTheCourse(),
                        form.getProficiencyExam(),
                        form.getQualifyingExam(),
                        form.getMaximumRegistrationDeadline(),
                        form.getDeadlineDissertation(),
                        form.getArticlesWritingPhase(),
                        form.getArticlesInEvaluation(),
                        form.getAcceptedArticles(),
                        form.getActivities(),
                        form.getResearchActivitiesResume(),
                        form.getAdditionalComments(),
                        form.getHasDifficulty(),
                        form.getVersion(),
                        form.getSubmissionDate(),
                        form.getAdvisorNote(),
                        form.getCcpOpinion(),
                        form.getStatusEvaluation()
                ))
                .collect(Collectors.toList());

    }

    public List<FormResponseDTO> findFormsByStudent(UUID studentId) {
        List<Form> forms = formRepository.findByStudent_id(studentId);
        return forms.stream()
                .map(form -> new FormResponseDTO(
                        form.getId(),
                        form.getStudentEmail(),
                        form.getStudentName(),
                        form.getAdvisor().getEmail(),
                        form.getUspNumber(),
                        form.getLattesLink(),
                        form.getLattesUpdateDate(),
                        form.getDiscipline(),
                        form.getEntryDate(),
                        form.getLastReportResult(),
                        form.getApprovalsFromTheBegginigOfTheCourse(),
                        form.getRepprovalsOnSecondSemester(),
                        form.getRepprovalsFromTheBegginigOfTheCourse(),
                        form.getProficiencyExam(),
                        form.getQualifyingExam(),
                        form.getMaximumRegistrationDeadline(),
                        form.getDeadlineDissertation(),
                        form.getArticlesWritingPhase(),
                        form.getArticlesInEvaluation(),
                        form.getAcceptedArticles(),
                        form.getActivities(),
                        form.getResearchActivitiesResume(),
                        form.getAdditionalComments(),
                        form.getHasDifficulty(),
                        form.getVersion(),
                        form.getSubmissionDate(),
                        form.getAdvisorNote(),
                        form.getCcpOpinion(),
                        form.getStatusEvaluation()
                ))
                .collect(Collectors.toList());

    }


}
