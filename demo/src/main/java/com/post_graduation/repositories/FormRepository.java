package com.post_graduation.repositories;

import com.post_graduation.domain.form.Form;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FormRepository extends JpaRepository<Form, UUID> {
}
