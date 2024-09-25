package com.pragma.emazon_cart.infrastructure.out.jpa.adapter;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import com.pragma.emazon_cart.domain.model.Cart;
import com.pragma.emazon_cart.infrastructure.out.jpa.entity.CartEntity;
import com.pragma.emazon_cart.infrastructure.out.jpa.mapper.CartEntityMapper;
import com.pragma.emazon_cart.infrastructure.out.jpa.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartJpaAdapterTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartEntityMapper cartEntityMapper;

    @InjectMocks
    private CartJpaAdapter cartJpaAdapter;

    private Cart cart;
    private CartEntity cartEntity;

    @BeforeEach
    void setUp() {
        cart = new Cart(
                1,
                123,
                new ArrayList<>(),
                new ArrayList<>(),
                LocalDate.now(),
                LocalDate.now()
        );

        cartEntity = new CartEntity(
                1,
                123,
                new ArrayList<>(),
                new ArrayList<>(),
                LocalDate.now(),
                LocalDate.now()
        );
    }

    @Test
    void givenCart_whenSaveCart_thenCartRepositorySaveIsCalled() {
        when(cartEntityMapper.toEntity(cart)).thenReturn(cartEntity);

        cartJpaAdapter.saveCart(cart);

        verify(cartRepository).save(cartEntity);
    }

    @Test
    void givenUserId_whenFindCartByUserId_thenReturnCart() {
        when(cartRepository.findByCartUserId(123)).thenReturn(Optional.of(cartEntity));
        when(cartEntityMapper.toDomain(cartEntity)).thenReturn(cart);

        Optional<Cart> result = cartJpaAdapter.findCartByUserId(123);

        assertTrue(result.isPresent());
        assertEquals(cart, result.get());
    }

    @Test
    void givenUserId_whenFindCartByUserIdNotFound_thenReturnEmpty() {
        when(cartRepository.findByCartUserId(123)).thenReturn(Optional.empty());

        Optional<Cart> result = cartJpaAdapter.findCartByUserId(123);

        assertTrue(result.isEmpty());
    }
}
