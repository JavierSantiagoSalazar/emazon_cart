package com.pragma.emazon_cart.domain.exceptions;

public class StockServiceUnavailableException extends RuntimeException {
    public StockServiceUnavailableException(String message) {
        super(message);
    }
}
