package com.post_graduation.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.post_graduation.domain.advisor.Advisor;
import com.post_graduation.dto.advisor.AdvisorHomeResponseDTO;
import com.post_graduation.dto.advisor.AdvisorRequestDTO;
import com.post_graduation.dto.advisor.AdvisorResponseDTO;
import com.post_graduation.dto.advisor.LoginAdvisorResponseDTO;
import com.post_graduation.dto.auth.LoginRequestDTO;
import com.post_graduation.dto.student.StudentResponseDTO;
import com.post_graduation.repositories.AdvisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdvisorService {

    @Autowired
    private AdvisorRepository repository;

    @Value("${api.security.token.secret}")
    private String secretKey;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginAdvisorResponseDTO create (AdvisorRequestDTO dto){
        this.repository.findByEmail(dto.email()).ifPresent(advisor -> {
            throw new RuntimeException("Orientador com email " + dto.email() + " já existe.");
        });

        Advisor advisor = new Advisor();
        advisor.setEmail(dto.email());
        advisor.setFirstName(dto.firstName());
        advisor.setLastName(dto.lastName());
        advisor.setPassword(passwordEncoder.encode(dto.password()));

        this.repository.save(advisor);

        var roles = Arrays.asList("ADVISOR");

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var expiresIn = Instant.now().plus(Duration.ofMinutes(10));
        var token = JWT.create()
                .withIssuer("javagas")
                .withSubject(advisor.getId().toString())
                .withClaim("roles", roles)
                .withExpiresAt(expiresIn)
                .sign(algorithm);

        return new LoginAdvisorResponseDTO(token, roles);

    }

    public AdvisorHomeResponseDTO getAdvisorById(UUID advisorId){
        Advisor advisor = this.repository.findById(advisorId).orElseThrow(() -> new RuntimeException("Advisor not found"));

        AdvisorHomeResponseDTO advisorHomeResponseDTO = new AdvisorHomeResponseDTO(advisor.getFirstName() + advisor.getLastName(), advisor.getEmail());

        System.out.println(advisorHomeResponseDTO.name());

        return advisorHomeResponseDTO;
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

    public LoginAdvisorResponseDTO login(LoginRequestDTO loginRequestDTO) throws AuthenticationException {
        var advisor = this.repository.findByEmail(loginRequestDTO.email())
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("Username/password incorrect");
                });

        var passwordMatches = this.passwordEncoder
                .matches(loginRequestDTO.password(), advisor.getPassword());

        if (!passwordMatches) {
            throw new AuthenticationException();
        }

        var roles = Arrays.asList("ADVISOR");

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var expiresIn = Instant.now().plus(Duration.ofMinutes(120));
        var token = JWT.create()
                .withIssuer("javagas")
                .withSubject(advisor.getId().toString())
                .withClaim("roles", roles)
                .withExpiresAt(expiresIn)
                .sign(algorithm);

        var AuthAdvisorResponse = new LoginAdvisorResponseDTO(token, roles);

        return AuthAdvisorResponse;
    }
}
