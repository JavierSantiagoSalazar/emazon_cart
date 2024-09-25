package com.pragma.emazon_cart.domain.spi;

import com.pragma.emazon_cart.domain.model.stock.Article;

import java.time.LocalDate;
import java.util.List;

public interface FeignClientPort {

    List<Article> getArticlesByIds(List<Integer> articleIdList);

    List<LocalDate> getRestockDates(List<Integer> articleIdList);

}
