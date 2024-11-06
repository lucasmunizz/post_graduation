package com.post_graduation.services;

import com.post_graduation.domain.subject.Subject;
import com.post_graduation.dto.advisor.AdvisorResponseDTO;
import com.post_graduation.dto.student.StudentResponseDTO;
import com.post_graduation.dto.subject.SubjectIdDTO;
import com.post_graduation.dto.subject.SubjectRequestDTO;
import com.post_graduation.dto.subject.SubjectResponseDTO;
import com.post_graduation.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository repository;


    public List<SubjectResponseDTO> findAll() {
        return this.repository.findAll().stream()
                .map(subject -> new SubjectResponseDTO(
                        subject.getId(),
                        subject.getName(),
                        subject.getStudentsApproved().stream()
                                .map(student -> new StudentResponseDTO(
                                        student.getId(),
                                        student.getUspNumber(),
                                        student.getFirstName(),
                                        student.getLastName(),
                                        student.getEmail(),
                                        student.getBirthDate(),
                                        student.getBirthSpot(),
                                        student.getNacionality(),
                                        student.getDiscipline(),
                                        student.getAdvisor_id().getEmail()
                                ))
                                .collect(Collectors.toList()),

                        subject.getStudentsRepproved().stream()
                                .map(student -> new StudentResponseDTO(
                                        student.getId(),
                                        student.getUspNumber(),
                                        student.getFirstName(),
                                        student.getLastName(),
                                        student.getEmail(),
                                        student.getBirthDate(),
                                        student.getBirthSpot(),
                                        student.getNacionality(),
                                        student.getDiscipline(),
                                        student.getAdvisor_id().getEmail()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public SubjectIdDTO create(SubjectRequestDTO dto){


        Subject subject = new Subject();
        subject.setName(dto.name());
        this.repository.save(subject);

        return new SubjectIdDTO(subject.getId());

    }
}
