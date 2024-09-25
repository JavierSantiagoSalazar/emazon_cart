package com.pragma.emazon_cart.application.handler;

import com.pragma.emazon_cart.application.dto.AddArticlesRequest;
import com.pragma.emazon_cart.application.mappers.AddArticlesRequestMapper;
import com.pragma.emazon_cart.domain.api.CartServicePort;
import com.pragma.emazon_cart.domain.model.AddArticles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class CartHandlerImpl implements CartHandler {

    private final CartServicePort cartServicePort;
    private final AddArticlesRequestMapper addArticlesRequestMapper;

    @Override
    public void addItemsToCart(List<AddArticlesRequest> addArticlesRequestList) {
        AddArticles addArticles = addArticlesRequestMapper.toDomain(addArticlesRequestList);
        cartServicePort.addItemsToCart(addArticles);
    }

}
