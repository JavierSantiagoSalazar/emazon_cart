package com.pragma.emazon_cart.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AddArticles {

    private List<Integer> articleIds;
    private List<Integer> articleAmounts;

}
