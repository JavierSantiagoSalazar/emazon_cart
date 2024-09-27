package com.pragma.emazon_cart.application.handler;

import com.pragma.emazon_cart.application.dto.AddArticlesRequest;
import com.pragma.emazon_cart.application.mappers.AddArticlesRequestMapper;
import com.pragma.emazon_cart.domain.api.CartServicePort;
import com.pragma.emazon_cart.domain.exceptions.InvalidInputException;
import com.pragma.emazon_cart.domain.model.AddArticles;
import com.pragma.emazon_cart.domain.utils.Constants;
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

    @Override
    public void deleteItemsFromCart(List<Integer> articlesIds) {
        if (articlesIds.stream().anyMatch(id -> id <= Constants.ZERO)) {
            throw new InvalidInputException(Constants.ARTICLES_IDS_MUST_BE_POSITIVE_ERROR_MESSAGE);
        }
        cartServicePort.deleteItemsFromCart(articlesIds);
    }

}
