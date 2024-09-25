package com.pragma.emazon_cart.application.dto.stock;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BrandResponse {

    private Integer brandId;
    private String brandName;
    private String brandDescription;

}
