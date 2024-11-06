package com.post_graduation.services;

import com.post_graduation.domain.advisor.Advisor;
import com.post_graduation.dto.advisor.AdvisorRequestDTO;
import com.post_graduation.dto.advisor.AdvisorResponseDTO;
import com.post_graduation.dto.student.StudentResponseDTO;
import com.post_graduation.repositories.AdvisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdvisorService {

    @Autowired
    private AdvisorRepository repository;

    public Advisor create (AdvisorRequestDTO dto){
        this.repository.findByEmail(dto.email()).ifPresent(advisor -> {
            throw new RuntimeException("Orientador com email " + dto.email() + " já existe.");
        });

        Advisor advisor = new Advisor();
        advisor.setEmail(dto.email());
        advisor.setFirstName(dto.firstName());
        advisor.setLastName(dto.lastName());
        advisor.setPassword(dto.password());

        this.repository.save(advisor);
        return advisor;
    }

    public List<AdvisorResponseDTO> findAll() {
        return this.repository.findAll().stream()
                .map(advisor -> new AdvisorResponseDTO(
                        advisor.getId(),
                        advisor.getFirstName(),
                        advisor.getLastName(),
                        advisor.getEmail(),
                        advisor.getStudents().stream()
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
}
