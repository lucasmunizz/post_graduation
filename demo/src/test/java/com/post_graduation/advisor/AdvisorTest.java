package com.post_graduation.advisor;

import com.post_graduation.controllers.AdvisorController;
import com.post_graduation.dto.advisor.AdvisorRequestDTO;
import com.post_graduation.dto.advisor.LoginAdvisorResponseDTO;
import com.post_graduation.services.AdvisorService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AdvisorTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AdvisorService advisorService;

    @BeforeEach
    public void setup(){
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void testCreateAdvisor() throws Exception {
        // Mock do retorno do service
        LoginAdvisorResponseDTO responseDTO = new LoginAdvisorResponseDTO(
                "fake-jwt-token",
                Arrays.asList("ADVISOR")
        );



        // JSON de entrada (requisito da API)
        String requestJson = """
            {
                "email": "test@email.com",
                "firstName": "Test",
                "lastName": "User",
                "password": "password123"
            }
        """;

        // Execução da requisição e verificações
        mvc.perform(MockMvcRequestBuilders.post("/advisors/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk());


    }
}
