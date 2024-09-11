package com.pragma.emazon_cart.infrastructure.input.rest;

import com.pragma.emazon_cart.domain.utils.Constants;
import com.pragma.emazon_cart.domain.utils.HttpStatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartRestController {

    @Operation(summary = Constants.ADD_TO_CART)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCode.OK,
                    description = Constants.ITEM_ADDED,
                    content = @Content
            )
    })
    @PostMapping("/")
    public String addItemToCart() {
        return "Item added to cart";
    }

    @Operation(summary = Constants.DELETE_FROM_CART)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCode.OK,
                    description = Constants.ITEM_DELETED,
                    content = @Content
            )
    })
    @DeleteMapping("/")
    public String deleteItemFromCart() {
        return "Item deleted from cart";
    }

    @Operation(summary = Constants.DELETE_FROM_CART)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCode.OK,
                    description = Constants.ITEM_DELETED,
                    content = @Content
            )
    })
    @DeleteMapping("/")
    public String deleteItemFromCart() {
        return "Item deleted from cart";
    }

}
