package com.pragma.emazon_cart.infrastructure.feing.stock;

import com.pragma.emazon_cart.application.dto.stock.ArticleResponse;
import com.pragma.emazon_cart.domain.utils.Constants;
import com.pragma.emazon_cart.infrastructure.configuration.bean.FeignCommonConfiguration;
import com.pragma.emazon_cart.infrastructure.configuration.bean.StockClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = Constants.STOCK_MICROSERVICE_NAME,
        url = "${emazon_stock.url}",
        configuration = { FeignCommonConfiguration.class, StockClientConfiguration.class }
)
public interface StockFeignClient {

    @GetMapping(value = "/get-articles-by-ids")
    List<ArticleResponse> getArticlesByIds(@RequestParam List<Integer> articleIdList);

}
