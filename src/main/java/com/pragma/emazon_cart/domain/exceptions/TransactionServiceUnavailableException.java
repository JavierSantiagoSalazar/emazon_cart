package com.pragma.emazon_cart.domain.exceptions;

public class TransactionServiceUnavailableException extends RuntimeException {
    public TransactionServiceUnavailableException(String message) {
        super(message);
    }
}
