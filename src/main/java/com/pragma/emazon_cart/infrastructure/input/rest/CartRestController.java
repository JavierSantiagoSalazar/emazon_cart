package com.pragma.emazon_cart.infrastructure.input.rest;

import com.pragma.emazon_cart.application.dto.ListAddRequest;
import com.pragma.emazon_cart.application.handler.CartHandler;
import com.pragma.emazon_cart.domain.utils.Constants;
import com.pragma.emazon_cart.domain.utils.HttpStatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartRestController {

    private final CartHandler cartHandler;

    @Operation(summary = Constants.ADD_TO_CART)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCode.OK,
                    description = Constants.ITEM_ADDED,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCode.NOT_FOUND,
                    description = Constants.ARTICLE_NOT_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCode.BAD_REQUEST,
                    description = Constants.INVALID_REQUEST,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCode.CONFLICT,
                    description = Constants.CATEGORY_LIMIT_EXCEEDED,
                    content = @Content
            )
    })
    @PostMapping("/")
    public ResponseEntity<Void> addItemToCart(@Valid @RequestBody ListAddRequest listAddRequest) {
        cartHandler.addItemsToCart(listAddRequest.getData());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = Constants.DELETE_FROM_CART)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCode.NO_CONTENT,
                    description = Constants.ITEM_DELETED,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCode.NOT_FOUND,
                    description = Constants.ARTICLE_NOT_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCode.BAD_REQUEST,
                    description = Constants.INVALID_REQUEST,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR,
                    description = Constants.INTERNAL_SERVER_ERROR,
                    content = @Content
            )
    })
    @DeleteMapping("/")
    public ResponseEntity<Void> deleteItemsFromCart(@RequestParam List<Integer> articlesIds) {
        cartHandler.deleteItemsFromCart(articlesIds);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = Constants.BUY_FROM_CART)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCode.OK,
                    description = Constants.ITEMS_PURCHASED,
                    content = @Content
            )
    })
    @PostMapping("/buy")
    public String buyItemsFromCart() {
        return "Items purchased from cart";
    }

}
