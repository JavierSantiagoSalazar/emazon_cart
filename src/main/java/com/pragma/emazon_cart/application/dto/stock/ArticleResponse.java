package com.pragma.emazon_cart.application.dto.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ArticleResponse {

    private Integer articleId;
    private String articleName;
    private String articleDescription;
    private Integer articleAmount;
    private Double articlePrice;
    private BrandResponse articleBrand;
    private List<EmbeddedCategoryResponse> articleCategories;

}
