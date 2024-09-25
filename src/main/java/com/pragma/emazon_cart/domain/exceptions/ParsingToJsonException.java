package com.pragma.emazon_cart.domain.exceptions;

public class ParsingToJsonException extends RuntimeException{
    public ParsingToJsonException(String message) {
        super(message);
    }
}
