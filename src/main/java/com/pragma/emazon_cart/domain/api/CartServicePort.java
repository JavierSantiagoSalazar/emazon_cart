package com.pragma.emazon_cart.domain.api;

import com.pragma.emazon_cart.domain.model.AddArticles;
import com.pragma.emazon_cart.domain.model.CartResponse;
import com.pragma.emazon_cart.domain.model.Pagination;

import java.util.List;

public interface CartServicePort {

    void addItemsToCart(AddArticles addArticles);

    void deleteItemsFromCart(List<Integer> articlesIds);

    Pagination<CartResponse> getAllArticlesFromCart(
            String sortOrder,
            String filterBy,
            String brandName,
            String categoryName,
            Integer page,
            Integer size
    );

}
