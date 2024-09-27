package com.pragma.emazon_cart.domain.model;

import com.pragma.emazon_cart.domain.model.stock.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    private Integer cartId;
    private Integer cartUserId;
    private List<Article> cartArticleList;
    private List<Integer> cartAmountList;
    private LocalDate cartCreationDate;
    private LocalDate cartLastUpdateDate;

}
