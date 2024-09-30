package com.pragma.emazon_cart.application.dto;

import com.pragma.emazon_cart.application.dto.stock.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDto {

    ArticleResponse cartArticle;
    Integer cartAmount;
    LocalDate restockDate;

}
