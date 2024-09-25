package com.pragma.emazon_cart.application.dto.stock;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmbeddedCategoryResponse {

    private Integer id;
    private String name;

}
