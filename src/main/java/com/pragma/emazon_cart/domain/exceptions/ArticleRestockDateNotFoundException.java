package com.pragma.emazon_cart.domain.exceptions;

public class ArticleRestockDateNotFoundException extends RuntimeException {
    public ArticleRestockDateNotFoundException(String message) {
        super(message);
    }
}
