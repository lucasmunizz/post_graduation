package com.post_graduation.infra.cors;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
    //@Override
    //public void addCorsMappings(CorsRegistry registry) {
        //registry.addMapping("/**")
        //        .allowedOrigins("*")
      //          .allowedMethods("GET", "POST", "DELETE", "PUT");
    //}
//}
@Component
public class CorsConfig implements CorsConfigurationSource {
    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        return config;
    }
}