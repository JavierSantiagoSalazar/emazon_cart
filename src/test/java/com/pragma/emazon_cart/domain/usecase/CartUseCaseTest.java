package com.pragma.emazon_cart.domain.usecase;

import com.pragma.emazon_cart.domain.exceptions.ArticleAlreadyExistsInCartException;
import com.pragma.emazon_cart.domain.exceptions.OutOfStockException;
import com.pragma.emazon_cart.domain.model.AddArticles;
import com.pragma.emazon_cart.domain.model.Cart;
import com.pragma.emazon_cart.domain.model.stock.Article;
import com.pragma.emazon_cart.domain.model.stock.Brand;
import com.pragma.emazon_cart.domain.spi.CartPersistencePort;
import com.pragma.emazon_cart.domain.spi.FeignClientPort;
import com.pragma.emazon_cart.domain.spi.TokenServicePort;
import com.pragma.emazon_cart.domain.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartUseCaseTest {

    @Mock
    private CartPersistencePort cartPersistencePort;

    @Mock
    private TokenServicePort tokenServicePort;

    @Mock
    private FeignClientPort feignClientPort;

    @InjectMocks
    private CartUseCase cartUseCase;

    private AddArticles addArticles;
    private List<Article> newArticles;
    private List<Integer> articleIds;

    @BeforeEach
    void setUp() {

        articleIds = Arrays.asList(1, 2);
        List<Integer> articleAmounts = Arrays.asList(3, 2);

        newArticles = Arrays.asList(
                new Article(
                        1,
                        "Article 1",
                        "Description 1",
                        5,
                        100.0,
                        new Brand(1,"Brand 1"),
                        new ArrayList<>()
                ),
                new Article(
                        2,
                        "Article 2",
                        "Description 2",
                        3,
                        150.0,
                        new Brand(2,"Brand 2"),
                        new ArrayList<>()
                )
        );

        addArticles = new AddArticles(articleIds, articleAmounts);
    }

    @Test
    void givenValidAddArticles_whenAddItemsToCart_thenSaveCartIsCalled() {

        when(feignClientPort.getArticlesByIds(articleIds)).thenReturn(newArticles);

        Integer userId = 123;
        when(tokenServicePort.extractUserIdFromToken()).thenReturn(userId);

        Cart existingCart = Cart.builder()
                .cartUserId(userId)
                .cartArticleList(new ArrayList<>())
                .cartAmountList(new ArrayList<>())
                .build();
        when(cartPersistencePort.findCartByUserId(userId)).thenReturn(Optional.of(existingCart));

        cartUseCase.addItemsToCart(addArticles);

        verify(feignClientPort).getArticlesByIds(articleIds);

        verify(tokenServicePort).extractUserIdFromToken();

        verify(cartPersistencePort).saveCart(existingCart);
    }

    @Test
    void givenOutOfStockItems_whenAddItemsToCart_thenOutOfStockExceptionIsThrown() {

        newArticles.get(0).setArticleAmount(1);

        when(feignClientPort.getArticlesByIds(articleIds)).thenReturn(newArticles);

        List<LocalDate> restockDates = Arrays.asList(LocalDate.now().plusDays(5), LocalDate.now().plusDays(10));
        when(feignClientPort.getRestockDates(anyList())).thenReturn(restockDates);

        OutOfStockException exception = assertThrows(OutOfStockException.class, () -> {
            cartUseCase.addItemsToCart(addArticles);
        });

        assertTrue(exception.getMessage().contains(Constants.CART_OUT_OF_STOCK_ARTICLES));
    }

    @Test
    void givenExistingArticleInCart_whenAddItemsToCart_thenArticleAlreadyExistsInCartExceptionIsThrown() {

        when(feignClientPort.getArticlesByIds(articleIds)).thenReturn(newArticles);

        Cart existingCart = Cart.builder()
                .cartUserId(123)
                .cartArticleList(Collections.singletonList(newArticles.get(0)))
                .cartAmountList(Collections.singletonList(2))
                .build();

        when(cartPersistencePort.findCartByUserId(anyInt())).thenReturn(Optional.of(existingCart));

        ArticleAlreadyExistsInCartException exception = assertThrows(ArticleAlreadyExistsInCartException.class, () -> {
            cartUseCase.addItemsToCart(addArticles);
        });

        assertEquals(Constants.ARTICLE_ALREADY_EXISTS_ERROR_MESSAGE, exception.getMessage());
    }
}
