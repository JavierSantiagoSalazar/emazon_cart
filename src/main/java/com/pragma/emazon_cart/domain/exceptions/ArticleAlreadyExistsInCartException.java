package com.pragma.emazon_cart.domain.exceptions;

public class ArticleAlreadyExistsInCartException extends RuntimeException {
    public ArticleAlreadyExistsInCartException(String message) {
        super(message);
    }
}
