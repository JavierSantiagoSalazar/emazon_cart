package com.pragma.emazon_cart.application.handler;

import com.pragma.emazon_cart.application.dto.AddArticlesRequest;
import com.pragma.emazon_cart.application.dto.CartResponseDto;
import com.pragma.emazon_cart.application.dto.stock.ArticleResponse;
import com.pragma.emazon_cart.application.dto.stock.BrandResponse;
import com.pragma.emazon_cart.application.dto.stock.EmbeddedCategoryResponse;
import com.pragma.emazon_cart.application.mappers.AddArticlesRequestMapper;
import com.pragma.emazon_cart.application.mappers.CartResponseDtoMapper;
import com.pragma.emazon_cart.domain.api.CartServicePort;
import com.pragma.emazon_cart.domain.exceptions.InvalidInputException;
import com.pragma.emazon_cart.domain.exceptions.PageOutOfBoundsException;
import com.pragma.emazon_cart.domain.model.AddArticles;
import com.pragma.emazon_cart.domain.model.CartResponse;
import com.pragma.emazon_cart.domain.model.Pagination;
import com.pragma.emazon_cart.domain.model.stock.Article;
import com.pragma.emazon_cart.domain.model.stock.Brand;
import com.pragma.emazon_cart.domain.model.stock.Category;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

    @Mock
    private CartResponseDtoMapper cartResponseDtoMapper;

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

    @Test
    void givenValidRequest_whenGetAllArticlesFromCart_thenReturnsPaginatedCartResponse() {

        List<CartResponse> cartResponseList = Arrays.asList(
                new CartResponse(
                        new Article(
                                1,
                                "Article 1",
                                "Description 1",
                                10,
                                100.0,
                                new Brand(1, "Brand 1"),
                                List.of(new Category(1, "Category 1"))
                        ),
                        5,
                        LocalDate.now().plusDays(15)
                ),
                new CartResponse(
                        new Article(
                                2,
                                "Article 2",
                                "Description 2",
                                5,
                                150.0,
                                new Brand(2, "Brand 2"),
                                List.of(new Category(2, "Category 2"))
                        ),
                        3,
                        LocalDate.now().plusDays(15)
                )
        );

        Pagination<CartResponse> paginationCartResponse = new Pagination<>(
                cartResponseList,
                950.0,
                1,
                10,
                2L,
                1,
                true
        );

        List<CartResponseDto> cartResponseDtoList = Arrays.asList(
                new CartResponseDto(
                        new ArticleResponse(
                                1,
                                "Article 1",
                                "Description 1",
                                1,
                                100.0,
                                new BrandResponse(1, "Brand 1"),
                                List.of(new EmbeddedCategoryResponse(1, "Category 1"))
                        ),
                        5,
                        LocalDate.now().plusDays(15)
                ),
                new CartResponseDto(
                        new ArticleResponse(
                                2,
                                "Article 2",
                                "Description 2",
                                1,
                                150.0,
                                new BrandResponse(2, "Brand 2"),
                                List.of(new EmbeddedCategoryResponse(2, "Category 2"))
                        ),
                        3,
                        LocalDate.now().plusDays(15)
                )
        );

        Pagination<CartResponseDto> expectedPaginationResponse = new Pagination<>(
                cartResponseDtoList,
                950.0,
                1,
                10,
                2L,
                1,
                true
        );

        when(cartServicePort.getAllArticlesFromCart("asc", "articleName", null, null, 1, 10))
                .thenReturn(paginationCartResponse);

        when(cartResponseDtoMapper.toResponseDto(any(CartResponse.class)))
                .thenReturn(
                        cartResponseDtoList.get(0),
                        cartResponseDtoList.get(1)
                );

        Pagination<CartResponseDto> actualPaginationResponse = cartHandlerImpl.getAllArticlesFromCart(
                "asc", "articleName", null, null, 1, 10);

        assertEquals(expectedPaginationResponse.getItems().size(), actualPaginationResponse.getItems().size());
        assertEquals(expectedPaginationResponse.getTotalPrice(), actualPaginationResponse.getTotalPrice());
        assertEquals(expectedPaginationResponse.getPageNo(), actualPaginationResponse.getPageNo());
        assertEquals(expectedPaginationResponse.getPageSize(), actualPaginationResponse.getPageSize());
        assertEquals(expectedPaginationResponse.getTotalItems(), actualPaginationResponse.getTotalItems());
        assertEquals(expectedPaginationResponse.getTotalPages(), actualPaginationResponse.getTotalPages());
        assertEquals(expectedPaginationResponse.getIsLastPage(), actualPaginationResponse.getIsLastPage());
    }

    @Test
    void givenPageOutOfBounds_whenGetAllArticlesFromCart_thenThrowsPageOutOfBoundsException() {
        when(cartServicePort.getAllArticlesFromCart("asc", "articleName", null, null, 3, 10))
                .thenThrow(new PageOutOfBoundsException(3, 1));

        PageOutOfBoundsException exception = assertThrows(PageOutOfBoundsException.class, () -> {
            cartHandlerImpl.getAllArticlesFromCart("asc", "articleName", null, null, 3, 10);
        });

        assertEquals("Page 3 is out of range. Total pages: 1", exception.getMessage());
    }

}
