package com.pragma.emazon_cart.infrastructure.input.rest;

import com.pragma.emazon_cart.application.dto.AddArticlesRequest;
import com.pragma.emazon_cart.application.dto.ListAddRequest;
import com.pragma.emazon_cart.application.handler.CartHandler;
import com.pragma.emazon_cart.infrastructure.configuration.security.filter.JwtValidatorFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
        AddArticlesRequest invalidArticle = new AddArticlesRequest(null, 2); // ID del artículo es null
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

}

