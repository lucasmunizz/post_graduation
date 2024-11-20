package com.post_graduation.repositories;

import com.post_graduation.domain.advisor.Advisor;
import com.post_graduation.domain.ccp.CCP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CCPRepository extends JpaRepository<CCP, UUID> {

    public Optional<CCP> findByEmail(String email);

}
