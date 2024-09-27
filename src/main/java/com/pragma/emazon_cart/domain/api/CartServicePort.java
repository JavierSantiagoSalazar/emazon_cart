package com.pragma.emazon_cart.domain.api;

import com.pragma.emazon_cart.domain.model.AddArticles;

import java.util.List;

public interface CartServicePort {

    void addItemsToCart(AddArticles addArticles);

    void deleteItemsFromCart(List<Integer> articlesIds);

}
