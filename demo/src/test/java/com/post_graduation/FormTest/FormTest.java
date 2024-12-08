package com.post_graduation.FormTest;

import com.post_graduation.domain.advisor.Advisor;
import com.post_graduation.domain.form.Form;
import com.post_graduation.domain.student.Student;
import com.post_graduation.dto.form.FormRequestDTO;
import com.post_graduation.repositories.AdvisorRepository;
import com.post_graduation.repositories.FormRepository;

import com.post_graduation.repositories.StudentRepository;
import com.post_graduation.services.FormService;
import com.post_graduation.services.MailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class FormTest {

    @Mock
    private FormRepository formRepository;

    @Mock
    private AdvisorRepository advisorRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private FormService formService;

    @Test
    void testResubmitForm() {
        UUID formId =  UUID.fromString("4b3a13cc-174e-46ae-8df9-c0ce5baa9dda");
        Form form = new Form();
        form.setId(formId);
        form.setStudentEmail("jpmc@email.com");

        Advisor advisor = new Advisor();
        advisor.setEmail("sarajaneperez@gmail.com");

        Student student = new Student();
        student.setUspNumber("13728942");

        // Mockando o repositório
        Mockito.when(formRepository.save(Mockito.any(Form.class))).thenReturn(form);
        Mockito.when(advisorRepository.findByEmail("sarajaneperez@gmail.com")).thenReturn(Optional.of(advisor));
        Mockito.when(studentRepository.findByUspNumber("13728942")).thenReturn(Optional.of(student));


        // Dados para o reenvio
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Form updatedForm = formService.create(new FormRequestDTO(
                "jpmc@email.com",
                "João Paulo de Camargo",
                "sarajaneperez@gmail.com",
                "13728942",
                "http://lattes.cnpq.br/1234567890",
                LocalDate.parse("30/05/2023", formatter), // Usando o formatter
                "Doutorado",
                LocalDate.parse("30/05/2023", formatter),
                "Aprovado",
                "10",
                "2",
                "3",
                "Passou",
                "Passou",
                LocalDate.parse("30/05/2023", formatter),
                LocalDate.parse("30/05/2023", formatter),
                "Em andamento",
                "2",
                "1",
                "Participação em seminários e workshops",
                "Pesquisa em inteligência artificial aplicada a saúde",
                "Necessita de mais orientação em estatística",
                "Não"
        ));

        // Verificações
        Assertions.assertNotNull(updatedForm);
        Assertions.assertEquals("jpmc@email.com", updatedForm.getStudentEmail());
        Mockito.verify(formRepository, Mockito.times(1)).save(Mockito.any(Form.class));
        Mockito.verify(advisorRepository, Mockito.times(1)).findByEmail("sarajaneperez@gmail.com");
        Mockito.verify(mailService, Mockito.times(1)).sendEmail(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString()
        );
    }
}
