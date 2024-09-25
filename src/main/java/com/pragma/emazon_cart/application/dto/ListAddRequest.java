package com.pragma.emazon_cart.application.dto;

import com.pragma.emazon_cart.domain.utils.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListAddRequest {

    @Valid
    @NotNull(message = Constants.DATA_LIST_CANNOT_BE_NULL)
    private List<AddArticlesRequest> data;

}
