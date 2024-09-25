package com.pragma.emazon_cart.application.handler;

import com.pragma.emazon_cart.application.dto.AddArticlesRequest;

import java.util.List;

public interface CartHandler {

    void addItemsToCart(List<AddArticlesRequest> addArticlesRequestList);

}
