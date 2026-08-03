package com.dat.productorder.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.dat.productorder.dto.response.common.ErrorResponse;
import java.time.LocalDateTime;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;

    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setErrors(null);
        response.setContentType("application/json");

        if (authException instanceof LockedException || authException instanceof DisabledException) {

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            errorResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            errorResponse.setMessage(authException.getMessage());

        } else {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            errorResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            errorResponse.setMessage("Invalid username or password");

        }

        objectMapper.writeValue(response.getWriter(), errorResponse);

    }
}
