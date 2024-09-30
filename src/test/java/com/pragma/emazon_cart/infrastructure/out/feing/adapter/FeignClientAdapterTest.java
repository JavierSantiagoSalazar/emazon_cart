package com.pragma.emazon_cart.infrastructure.out.feing.adapter;

import com.pragma.emazon_cart.application.dto.stock.ArticleResponse;
import com.pragma.emazon_cart.application.dto.stock.BrandResponse;
import com.pragma.emazon_cart.application.dto.stock.EmbeddedCategoryResponse;
import com.pragma.emazon_cart.domain.model.stock.Article;
import com.pragma.emazon_cart.domain.model.stock.Brand;
import com.pragma.emazon_cart.infrastructure.feing.stock.StockFeignClient;
import com.pragma.emazon_cart.infrastructure.feing.transaction.TransactionFeignClient;
import com.pragma.emazon_cart.infrastructure.out.feing.mapper.ArticleResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FeignClientAdapterTest {

    @Mock
    private StockFeignClient stockFeignClient;

    @Mock
    private TransactionFeignClient transactionFeignClient;

    @Mock
    private ArticleResponseMapper articleResponseMapper;

    @InjectMocks
    private FeignClientAdapter feignClientAdapter;

    private List<Integer> articleIdList;
    private List<ArticleResponse> articleResponseList;
    private List<Article> articles;

    @BeforeEach
    void setUp() {
        articleIdList = Arrays.asList(1, 2);

        articleResponseList = Arrays.asList(
                new ArticleResponse(
                        1,
                        "Article 1",
                        "Description 1",
                        5,
                        100.0,
                        new BrandResponse(1, "Brand 1"),
                        Collections.singletonList(new EmbeddedCategoryResponse(1, "Category 1"))
                ),
                new ArticleResponse(
                        2,
                        "Article 2",
                        "Description 2",
                        3,
                        150.0,
                        new BrandResponse(2, "Brand 2"),
                        Collections.singletonList(new EmbeddedCategoryResponse(2, "Category 2"))
                )
        );

        articles = Arrays.asList(
                new Article(
                        1,
                        "Article 1",
                        "Description 1",
                        5,
                        100.0,
                        new Brand(1, "Brand 1"), Collections.emptyList()
                ),
                new Article(
                        2,
                        "Article 2",
                        "Description 2",
                        3,
                        150.0,
                        new Brand(2, "Brand 2"), Collections.emptyList()
                )
        );
    }

    @Test
    void givenArticleIds_whenGetArticlesByIds_thenReturnsArticles() {

        when(stockFeignClient.getArticlesByIds(articleIdList)).thenReturn(articleResponseList);
        when(articleResponseMapper.toDomain(articleResponseList)).thenReturn(articles);

        List<Article> result = feignClientAdapter.getArticlesByIds(articleIdList);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(stockFeignClient).getArticlesByIds(articleIdList);
        verify(articleResponseMapper).toDomain(articleResponseList);
    }

    @Test
    void givenArticleIds_whenGetRestockDates_thenReturnsRestockDates() {

        List<LocalDate> restockDates = Arrays.asList(LocalDate.now().plusDays(5), LocalDate.now().plusDays(10));
        when(transactionFeignClient.getRestockDate(articleIdList)).thenReturn(restockDates);

        List<LocalDate> result = feignClientAdapter.getRestockDates(articleIdList);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(transactionFeignClient).getRestockDate(articleIdList);
    }

}

