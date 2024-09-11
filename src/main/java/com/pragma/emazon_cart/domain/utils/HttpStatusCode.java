package com.pragma.emazon_cart.domain.utils;

import lombok.Getter;

@Getter
public class HttpStatusCode {

    private HttpStatusCode() {
        throw new IllegalStateException("Utility class");
    }

    public static final String OK = "200";

}
