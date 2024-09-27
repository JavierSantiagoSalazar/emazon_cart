package com.pragma.emazon_cart.application.handler;

import com.pragma.emazon_cart.application.dto.AddArticlesRequest;
import com.pragma.emazon_cart.application.mappers.AddArticlesRequestMapper;
import com.pragma.emazon_cart.domain.api.CartServicePort;
import com.pragma.emazon_cart.domain.exceptions.InvalidInputException;
import com.pragma.emazon_cart.domain.model.AddArticles;
import com.pragma.emazon_cart.domain.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
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
    private List<Integer> validArticlesIds;
    private List<Integer> invalidArticlesIds;

    @BeforeEach
    void setUp() {

        addArticlesRequestList = new ArrayList<>();
        addArticlesRequestList.add(new AddArticlesRequest(1, 5));
        addArticlesRequestList.add(new AddArticlesRequest(2, 3));

        List<Integer> articleIds = Arrays.asList(1, 2);
        List<Integer> articleAmounts = Arrays.asList(5, 3);
        addArticles = new AddArticles(articleIds, articleAmounts);

        validArticlesIds = Arrays.asList(1, 2, 3);
        invalidArticlesIds = Arrays.asList(1, -2, 3);
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

    @Test
    void givenValidArticlesIds_whenDeleteItemsFromCart_thenCartServicePortIsCalled() {

        cartHandlerImpl.deleteItemsFromCart(validArticlesIds);

        verify(cartServicePort).deleteItemsFromCart(validArticlesIds);
    }

    @Test
    void givenInvalidArticlesIds_whenDeleteItemsFromCart_thenInvalidInputExceptionIsThrown() {

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                cartHandlerImpl.deleteItemsFromCart(invalidArticlesIds)
        );

        assertEquals(Constants.ARTICLES_IDS_MUST_BE_POSITIVE_ERROR_MESSAGE, exception.getMessage());
        verify(cartServicePort, never()).deleteItemsFromCart(anyList());
    }

    @Test
    void givenEmptyArticlesIdsList_whenDeleteItemsFromCart_thenCartServicePortIsCalled() {

        List<Integer> emptyArticlesIds = new ArrayList<>();

        cartHandlerImpl.deleteItemsFromCart(emptyArticlesIds);

        verify(cartServicePort).deleteItemsFromCart(emptyArticlesIds);
    }

    @Test
    void givenZeroArticleId_whenDeleteItemsFromCart_thenInvalidInputExceptionIsThrown() {
        List<Integer> articlesIdsWithZero = Arrays.asList(1, 0, 3);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                cartHandlerImpl.deleteItemsFromCart(articlesIdsWithZero)
        );

        assertEquals(Constants.ARTICLES_IDS_MUST_BE_POSITIVE_ERROR_MESSAGE, exception.getMessage());
        verify(cartServicePort, never()).deleteItemsFromCart(anyList());
    }
}