package com.pragma.emazon_cart.domain.spi;

import com.pragma.emazon_cart.domain.model.Cart;

import java.util.Optional;

public interface CartPersistencePort {

    void saveCart(Cart cart);

    Optional<Cart> findCartByUserId(Integer userId);

}
