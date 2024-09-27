package com.pragma.emazon_cart.domain.utils;

import lombok.Getter;

@Getter
public class HttpStatusCode {

    private HttpStatusCode() {
        throw new IllegalStateException("Utility class");
    }

    public static final String OK = "200";
    public static final String NO_CONTENT = "204";
    public static final String NOT_FOUND = "404";
    public static final String CONFLICT = "409";
    public static final String BAD_REQUEST = "400";
    public static final String INTERNAL_SERVER_ERROR = "500";

}
