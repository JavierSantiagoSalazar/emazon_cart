package com.pragma.emazon_cart.infrastructure.configuration.security.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.emazon_cart.domain.utils.Constants;
import com.pragma.emazon_cart.infrastructure.configuration.exception.dto.Response;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        Response customResponse = Response.builder()
                .statusCode(HttpStatus.UNAUTHORIZED)
                .message(Constants.UNAUTHORIZED_ACCESS + authException.getMessage())
                .build();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(customResponse);

        response.getWriter().write(jsonResponse);

    }

}
