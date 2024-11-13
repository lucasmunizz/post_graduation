package com.post_graduation.repositories;

import com.post_graduation.domain.form.Form;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FormRepository extends JpaRepository<Form, UUID> {

    List<Form> findByAdvisor_id(UUID advisorId);

    List<Form> findByStudent_id(UUID studentId);
}
