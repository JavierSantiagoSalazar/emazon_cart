package com.pragma.emazon_cart.domain.model;

import com.pragma.emazon_cart.domain.model.stock.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class CartResponse {

    Article cartArticle;
    Integer cartAmount;
    LocalDate restockDate;

}
