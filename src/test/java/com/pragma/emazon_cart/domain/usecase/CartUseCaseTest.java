package com.pragma.emazon_cart.domain.usecase;

import com.pragma.emazon_cart.domain.exceptions.ArticleAlreadyExistsInCartException;
import com.pragma.emazon_cart.domain.exceptions.ArticleNotFoundException;
import com.pragma.emazon_cart.domain.exceptions.CartNotFoundException;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Test
    void givenNonExistingArticleIds_whenDeleteItemsFromCart_thenArticleNotFoundExceptionIsThrown() {
        Integer userId = 123;
        List<Integer> articleIdList = Arrays.asList(4, 5);
        Cart cart = Cart.builder()
                .cartUserId(userId)
                .cartArticleList(newArticles)
                .cartAmountList(Arrays.asList(2, 3))
                .build();

        when(tokenServicePort.extractUserIdFromToken()).thenReturn(userId);
        when(cartPersistencePort.findCartByUserId(userId)).thenReturn(Optional.of(cart));

        ArticleNotFoundException exception = assertThrows(ArticleNotFoundException.class, () -> {
            cartUseCase.deleteItemsFromCart(articleIdList);
        });

        assertEquals(Constants.ARTICLES_NOT_FOUND, exception.getMessage());
    }

    @Test
    void givenValidArticleIds_whenDeleteItemsFromCart_thenArticlesAndAmountsAreRemoved() {
        Integer userId = 123;
        List<Integer> articleIdsToRemove = Arrays.asList(1, 2);

        List<Article> articles = Arrays.asList(
                new Article(1, "Article 1", "Description 1", 5, 100.0, new Brand(1, "Brand 1"), new ArrayList<>()),
                new Article(2, "Article 2", "Description 2", 3, 150.0, new Brand(2, "Brand 2"), new ArrayList<>())
        );

        Cart cart = Cart.builder()
                .cartId(1)
                .cartUserId(userId)
                .cartArticleList(articles)
                .cartAmountList(Arrays.asList(2, 3))
                .build();

        when(tokenServicePort.extractUserIdFromToken()).thenReturn(userId);
        when(cartPersistencePort.findCartByUserId(userId)).thenReturn(Optional.of(cart));

        cartUseCase.deleteItemsFromCart(articleIdsToRemove);

        assertTrue(cart.getCartArticleList().isEmpty());
        assertTrue(cart.getCartAmountList().isEmpty());
        verify(cartPersistencePort).saveCart(cart);
    }

    @Test
    void givenNonExistingCart_whenDeleteItemsFromCart_thenCartNotFoundExceptionIsThrown() {
        Integer userId = 123;

        when(tokenServicePort.extractUserIdFromToken()).thenReturn(userId);
        when(cartPersistencePort.findCartByUserId(userId)).thenReturn(Optional.empty());

        CartNotFoundException exception = assertThrows(CartNotFoundException.class, () -> {
            cartUseCase.deleteItemsFromCart(articleIds);
        });

        assertEquals(Constants.CART_NOT_FOUND_ERROR_MESSAGE + userId, exception.getMessage());
    }

    @Test
    void givenNewCart_whenAddItemsToCart_thenCartCreatedWithNewItems() {

        Integer userId = 123;
        List<Integer> articleIdList = Arrays.asList(1, 2);
        List<Integer> articleAmounts = Arrays.asList(5, 3);

        Article article1 = new Article(1, "Article 1", "Description 1", 10, 100.0, null, List.of());
        Article article2 = new Article(2, "Article 2", "Description 2", 8, 200.0, null, List.of());

        List<Article> articlesList = Arrays.asList(article1, article2);

        when(tokenServicePort.extractUserIdFromToken()).thenReturn(userId);
        when(cartPersistencePort.findCartByUserId(userId)).thenReturn(Optional.empty());
        when(feignClientPort.getArticlesByIds(articleIdList)).thenReturn(articlesList);

        cartUseCase.addItemsToCart(new AddArticles(articleIdList, articleAmounts));

        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
        verify(cartPersistencePort).saveCart(cartCaptor.capture());

        Cart savedCart = cartCaptor.getValue();

        assertEquals(userId, savedCart.getCartUserId());
        assertEquals(2, savedCart.getCartArticleList().size());
        assertEquals(articlesList, savedCart.getCartArticleList());
        assertEquals(articleAmounts, savedCart.getCartAmountList());
    }

}
