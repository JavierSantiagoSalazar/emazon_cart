package com.pragma.emazon_cart.application.dto;

import com.pragma.emazon_cart.application.handler.CartHandlerImpl;
import com.pragma.emazon_cart.application.mappers.AddArticlesRequestMapper;
import com.pragma.emazon_cart.domain.api.CartServicePort;
import com.pragma.emazon_cart.domain.model.AddArticles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartHandlerImplTest {

    @Mock
    private CartServicePort cartServicePort;

    @Mock
    private AddArticlesRequestMapper addArticlesRequestMapper;

    @InjectMocks
    private CartHandlerImpl cartHandlerImpl;

    private List<AddArticlesRequest> addArticlesRequestList;
    private AddArticles addArticles;

    @BeforeEach
    void setUp() {

        addArticlesRequestList = new ArrayList<>();
        addArticlesRequestList.add(new AddArticlesRequest(1, 5));
        addArticlesRequestList.add(new AddArticlesRequest(2, 3));

        List<Integer> articleIds = Arrays.asList(1, 2);
        List<Integer> articleAmounts = Arrays.asList(5, 3);
        addArticles = new AddArticles(articleIds, articleAmounts);
    }

    @Test
    void givenValidAddArticlesRequestList_whenAddItemsToCart_thenCartServicePortIsCalled() {

        when(addArticlesRequestMapper.toDomain(addArticlesRequestList)).thenReturn(addArticles);

        cartHandlerImpl.addItemsToCart(addArticlesRequestList);

        verify(addArticlesRequestMapper).toDomain(addArticlesRequestList);

        verify(cartServicePort).addItemsToCart(addArticles);
    }

    @Test
    void givenEmptyAddArticlesRequestList_whenAddItemsToCart_thenCartServicePortIsNotCalled() {

        List<AddArticlesRequest> emptyList = new ArrayList<>();

        AddArticles emptyAddArticles = new AddArticles(new ArrayList<>(), new ArrayList<>());

        when(addArticlesRequestMapper.toDomain(emptyList)).thenReturn(emptyAddArticles);

        cartHandlerImpl.addItemsToCart(emptyList);

        verify(addArticlesRequestMapper).toDomain(emptyList);

        verify(cartServicePort).addItemsToCart(emptyAddArticles);
    }
}