package com.pragma.emazon_cart.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Pagination<T> {

    private List<T> items;
    private Double totalPrice;
    private Integer pageNo;
    private Integer pageSize;
    private Long totalItems;
    private Integer totalPages;
    private Boolean isLastPage;

}
