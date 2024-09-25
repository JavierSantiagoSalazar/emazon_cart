package com.pragma.emazon_cart.domain.model.stock;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Article {

    private Integer articleId;
    private String articleName;
    private String articleDescription;
    private Integer articleAmount;
    private Double articlePrice;
    private Brand articleBrand;
    private List<Category> articleCategories;

}
