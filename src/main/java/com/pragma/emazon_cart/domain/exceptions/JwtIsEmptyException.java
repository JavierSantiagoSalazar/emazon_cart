package com.pragma.emazon_cart.domain.exceptions;

public class JwtIsEmptyException extends RuntimeException{
    public JwtIsEmptyException(String message) {
        super(message);
    }
}
