package com.pragma.emazon_cart.infrastructure.input.rest;

import com.pragma.emazon_cart.application.dto.AddArticlesRequest;
import com.pragma.emazon_cart.application.dto.CartResponseDto;
import com.pragma.emazon_cart.application.dto.ListAddRequest;
import com.pragma.emazon_cart.application.handler.CartHandler;
import com.pragma.emazon_cart.domain.model.Pagination;
import com.pragma.emazon_cart.infrastructure.configuration.security.filter.JwtValidatorFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = CartRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class CartRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartHandler cartHandler;

    @MockBean
    private JwtValidatorFilter jwtValidatorFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private ListAddRequest listAddRequest;

    @BeforeEach
    public void setUp() {
        AddArticlesRequest addArticlesRequest1 = new AddArticlesRequest(1, 2);
        AddArticlesRequest addArticlesRequest2 = new AddArticlesRequest(3, 4);
        listAddRequest = new ListAddRequest(List.of(addArticlesRequest1, addArticlesRequest2));
    }

    @Test
    void givenValidListAddRequest_whenAddItemToCartIsCalled_thenReturns204() throws Exception {
        mockMvc.perform(post("/cart/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listAddRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenNullData_whenAddItemToCartIsCalled_thenReturns400() throws Exception {
        listAddRequest.setData(null);

        mockMvc.perform(post("/cart/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listAddRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("[The data send cannot be null]"));
    }

    @Test
    void givenInvalidArticleId_whenAddItemToCartIsCalled_thenReturns400() throws Exception {
        AddArticlesRequest invalidArticle = new AddArticlesRequest(null, 2);
        listAddRequest.setData(List.of(invalidArticle));

        mockMvc.perform(post("/cart/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listAddRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("[Article ID cannot be null]"));
    }

    @Test
    void givenNegativeArticleId_whenAddItemToCartIsCalled_thenReturns400() throws Exception {
        AddArticlesRequest invalidArticle = new AddArticlesRequest(-1, 2);
        listAddRequest.setData(List.of(invalidArticle));

        mockMvc.perform(post("/cart/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listAddRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(
                        "[Article ID must be a positive number]"
                ));
    }

    @Test
    void givenInvalidArticleAmount_whenAddItemToCartIsCalled_thenReturns400() throws Exception {
        AddArticlesRequest invalidArticle = new AddArticlesRequest(1, null);
        listAddRequest.setData(List.of(invalidArticle));

        mockMvc.perform(post("/cart/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listAddRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("[Article amount cannot be null]"));
    }

    @Test
    void givenNegativeArticleAmount_whenAddItemToCartIsCalled_thenReturns400() throws Exception {
        AddArticlesRequest invalidArticle = new AddArticlesRequest(1, -5);
        listAddRequest.setData(List.of(invalidArticle));

        mockMvc.perform(post("/cart/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listAddRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("[Article amount must be a positive number]"));
    }

    @Test
    void givenMalformedJson_whenAddItemToCartIsCalled_thenReturns400() throws Exception {
        mockMvc.perform(post("/cart/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalidJson}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenValidArticleIds_whenDeleteItemsFromCartIsCalled_thenReturns204() throws Exception {

        List<Integer> articleIds = List.of(1, 2, 3);

        mockMvc.perform(delete("/cart/")
                        .param("articlesIds", articleIds.stream()
                                .map(String::valueOf).toArray(String[]::new)))
                .andExpect(status().isNoContent());

        Mockito.verify(cartHandler).deleteItemsFromCart(articleIds);
    }

    @Test
    void givenEmptyArticleIds_whenDeleteItemsFromCartIsCalled_thenReturns400() throws Exception {

        mockMvc.perform(delete("/cart/"))
                .andExpect(status().isBadRequest());

        Mockito.verify(cartHandler, Mockito.never()).deleteItemsFromCart(Mockito.anyList());
    }

    @Test
    void givenInvalidParameterFormat_whenDeleteItemsFromCartIsCalled_thenReturns400() throws Exception {

        mockMvc.perform(delete("/cart/")
                        .param("articlesIds", "invalidId"))
                .andExpect(status().isBadRequest());

        Mockito.verify(cartHandler, Mockito.never()).deleteItemsFromCart(Mockito.anyList());
    }

    @Test
    void givenMissingOptionalParams_whenGetArticlesIsCalled_thenReturns200() throws Exception {
        Pagination<CartResponseDto> pagination = new Pagination<>(
                List.of(new CartResponseDto()),
                100.0,
                1,
                10,
                1L,
                1,
                true
        );

        Mockito.when(cartHandler.getAllArticlesFromCart(
                Mockito.anyString(), Mockito.anyString(),
                Mockito.isNull(), Mockito.isNull(),
                Mockito.anyInt(), Mockito.anyInt())
        ).thenReturn(pagination);

        mockMvc.perform(get("/cart/")
                        .param("sortOrder", "asc")
                        .param("filterBy", "articleName")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.totalPrice").value(100.0))
                .andExpect(jsonPath("$.totalItems").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.isLastPage").value(true));
    }

    @Test
    void givenInvalidPage_whenGetArticlesIsCalled_thenReturns400() throws Exception {
        mockMvc.perform(get("/cart/")
                        .param("sortOrder", "asc")
                        .param("filterBy", "articleName")
                        .param("page", "-1")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidSize_whenGetArticlesIsCalled_thenReturns400() throws Exception {
        mockMvc.perform(get("/cart/")
                        .param("sortOrder", "asc")
                        .param("filterBy", "articleName")
                        .param("page", "1")
                        .param("size", "-10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenNoArticlesInCart_whenGetArticlesIsCalled_thenReturns200() throws Exception {
        Pagination<CartResponseDto> emptyPagination = new Pagination<>(
                List.of(), 0.0, 1, 10, 0L, 0, true);

        Mockito.when(cartHandler.getAllArticlesFromCart(
                Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString(),
                Mockito.anyInt(), Mockito.anyInt())
        ).thenReturn(emptyPagination);

        mockMvc.perform(get("/cart/")
                        .param("sortOrder", "asc")
                        .param("filterBy", "articleName")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

}

