package com.pragma.emazon_cart.domain.exceptions;

public class ArticleAmountMismatchException extends RuntimeException {
    public ArticleAmountMismatchException(String message) {
        super(message);
    }
}
