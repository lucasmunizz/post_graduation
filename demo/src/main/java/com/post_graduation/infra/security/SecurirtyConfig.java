package com.post_graduation.infra.security;

import com.post_graduation.infra.cors.CorsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurirtyConfig {



    @Autowired
    SecurityAdvisorFilter securityAdvisorFilter;

    @Autowired
    SecurityStudentFilter securityStudentFilter;

    @Autowired
    CorsConfig corsConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/students/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/advisors/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/students/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/advisors/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/subjects/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/advisors/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/advisors/*/forms").permitAll()
                        .anyRequest().authenticated()
                        //.anyRequest().permitAll()
                )
                .cors(c -> c.configurationSource(corsConfig))
                .addFilterBefore(securityStudentFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(securityAdvisorFilter, BasicAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}


