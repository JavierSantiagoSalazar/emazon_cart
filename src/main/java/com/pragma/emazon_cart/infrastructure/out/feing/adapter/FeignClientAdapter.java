package com.pragma.emazon_cart.infrastructure.out.feing.adapter;

import com.pragma.emazon_cart.application.dto.stock.ArticleResponse;
import com.pragma.emazon_cart.domain.model.stock.Article;
import com.pragma.emazon_cart.domain.spi.FeignClientPort;
import com.pragma.emazon_cart.infrastructure.feing.stock.StockFeignClient;
import com.pragma.emazon_cart.infrastructure.feing.transaction.TransactionFeignClient;
import com.pragma.emazon_cart.infrastructure.out.feing.mapper.ArticleResponseMapper;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class FeignClientAdapter implements FeignClientPort {

    private final StockFeignClient stockFeignClient;
    private final TransactionFeignClient transactionFeignClient;
    private final ArticleResponseMapper articleResponseMapper;

    @Override
    public List<Article> getArticlesByIds(List<Integer> articleIdList) {
        List<ArticleResponse> articleResponseList = stockFeignClient.getArticlesByIds(articleIdList);
        return articleResponseMapper.toDomain(articleResponseList);
    }

    @Override
    public List<LocalDate> getRestockDates(List<Integer> articleIdList) {
        return transactionFeignClient.getRestockDate(articleIdList);
    }
}
