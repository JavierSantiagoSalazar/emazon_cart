package com.pragma.emazon_cart.domain.api;

import com.pragma.emazon_cart.domain.model.AddArticles;

public interface CartServicePort {

    void addItemsToCart(AddArticles addArticles);

}
