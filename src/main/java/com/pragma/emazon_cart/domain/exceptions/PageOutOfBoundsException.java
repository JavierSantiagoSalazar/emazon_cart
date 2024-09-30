package com.pragma.emazon_cart.domain.exceptions;

import com.pragma.emazon_cart.domain.utils.Constants;
import lombok.Getter;

@Getter
public class PageOutOfBoundsException extends RuntimeException{

    private final Integer requestedPage;
    private final Integer totalPages;

    public PageOutOfBoundsException(Integer requestedPage, Integer totalPages) {
        super(Constants.PAGE_OUT_OF_BOUNDS_PAGE + requestedPage + Constants.PAGE_OUT_OF_BOUNDS_TOTAL_PAGES + totalPages);
        this.requestedPage = requestedPage;
        this.totalPages = totalPages;
    }
}
