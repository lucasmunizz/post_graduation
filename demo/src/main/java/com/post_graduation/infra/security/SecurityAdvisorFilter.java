package com.post_graduation.infra.security;

import com.post_graduation.infra.providers.JwtAdvisorProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityAdvisorFilter extends OncePerRequestFilter {

    @Autowired
    private JwtAdvisorProvider jwtAdvisorProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // SecurityContextHolder.getContext().setAuthentication(null);
        String header = request.getHeader("Authorization");

        if (request.getRequestURI().startsWith("/company") || request.getRequestURI().startsWith("/api") ) {
            if (header != null) {
                var token = this.jwtAdvisorProvider.validateToken(header);
                if (token == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                var roles = token.getClaim("roles").asList(Object.class);

                var grants = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase()))
                        .toList();

                request.setAttribute("advisor_id", token.getSubject());
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(token.getSubject(), null,
                        grants);

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
