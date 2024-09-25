package com.pragma.emazon_cart.domain.exceptions;

import static com.pragma.emazon_cart.domain.utils.Constants.CATEGORY_LIMIT_EXCEEDED_PREFIX_ERROR_MESSAGE;
import static com.pragma.emazon_cart.domain.utils.Constants.CATEGORY_LIMIT_EXCEEDED_SUFFIX_ERROR_MESSAGE;

public class CategoryLimitExceededException extends RuntimeException {
    public CategoryLimitExceededException(String category) {
        super(CATEGORY_LIMIT_EXCEEDED_PREFIX_ERROR_MESSAGE + category + CATEGORY_LIMIT_EXCEEDED_SUFFIX_ERROR_MESSAGE);
    }
}