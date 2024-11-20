package com.post_graduation.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.post_graduation.domain.advisor.Advisor;
import com.post_graduation.domain.ccp.CCP;
import com.post_graduation.dto.advisor.AdvisorRequestDTO;
import com.post_graduation.dto.advisor.LoginAdvisorResponseDTO;
import com.post_graduation.dto.auth.LoginRequestDTO;
import com.post_graduation.dto.ccp.CCPLoginRequestDTO;
import com.post_graduation.dto.ccp.CCPLoginResponseDTO;
import com.post_graduation.repositories.AdvisorRepository;
import com.post_graduation.repositories.CCPRepository;
import com.post_graduation.repositories.StudentRepository;
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
import java.util.stream.Collectors;

@Service
public class CCPService {

    @Autowired
    private CCPRepository repository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private MailService mailService;

    @Value("${api.security.token.secret}")
    private String secretKey;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CCPLoginResponseDTO create (CCPLoginRequestDTO dto){
        this.repository.findByEmail(dto.email()).ifPresent(ccp -> {
            throw new RuntimeException("Conta de CPP com email " + dto.email() + " já existe.");
        });

        CCP ccp = new CCP();
        ccp.setEmail(dto.email());
        ccp.setPassword(passwordEncoder.encode(dto.password()));

        this.repository.save(ccp);

        var roles = Arrays.asList("CCP");

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var expiresIn = Instant.now().plus(Duration.ofMinutes(10));
        var token = JWT.create()
                .withIssuer("javagas")
                .withSubject(ccp.getId().toString())
                .withClaim("roles", roles)
                .withExpiresAt(expiresIn)
                .sign(algorithm);

        return new CCPLoginResponseDTO(token, roles);

    }

    public CCPLoginResponseDTO login(CCPLoginRequestDTO loginRequestDTO) throws AuthenticationException {
        var ccp = this.repository.findByEmail(loginRequestDTO.email())
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("Username/password incorrect");
                });

        var passwordMatches = this.passwordEncoder
                .matches(loginRequestDTO.password(), ccp.getPassword());

        if (!passwordMatches) {
            throw new AuthenticationException();
        }

        var roles = Arrays.asList("CCP");

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var expiresIn = Instant.now().plus(Duration.ofMinutes(120));
        var token = JWT.create()
                .withIssuer("javagas")
                .withSubject(ccp.getId().toString())
                .withClaim("roles", roles)
                .withExpiresAt(expiresIn)
                .sign(algorithm);

        var CCPLoginResponseDTO = new CCPLoginResponseDTO(token, roles);

        return CCPLoginResponseDTO;
    }

    public void generalOpinion(){

        List<String> studentEmails = studentRepository.findAll().stream()
                .map(student -> student.getEmail())
                .collect(Collectors.toList());

        // Buscar todos os e-mails de orientadores
        List<String> advisorEmails = advisorRepository.findAll().stream()
                .map(advisor -> advisor.getEmail())
                .collect(Collectors.toList());

        // Combinar todos os e-mails
        List<String> allEmails = studentEmails;
        allEmails.addAll(advisorEmails);
        allEmails.add("posgraduacaosistema@gmail.com");

        this.mailService.sendEmailWithManyRecipients(allEmails, "Parecer CCP", "Caros alunos e orientadores, foi finalizada a avaliação de todos os relatórios, para verificar, acesse o sistema");

    }
}
