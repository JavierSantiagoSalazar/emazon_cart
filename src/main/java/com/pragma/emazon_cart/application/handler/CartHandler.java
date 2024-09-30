package com.pragma.emazon_cart.application.handler;

import com.pragma.emazon_cart.application.dto.AddArticlesRequest;
import com.pragma.emazon_cart.application.dto.CartResponseDto;
import com.pragma.emazon_cart.domain.model.Pagination;

import java.util.List;

public interface CartHandler {

    void addItemsToCart(List<AddArticlesRequest> addArticlesRequestList);

    void deleteItemsFromCart(List<Integer> articlesIds);

    Pagination<CartResponseDto> getAllArticlesFromCart(
            String sortOrder,
            String filterBy,
            String brandName,
            String categoryName,
            Integer page,
            Integer size
    );

}
