package com.pragma.emazon_cart.infrastructure.configuration.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    private HttpStatus statusCode;
    private String message;

}
