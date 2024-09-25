package com.pragma.emazon_cart.infrastructure.out.jpa.adapter;

import com.pragma.emazon_cart.domain.model.Cart;
import com.pragma.emazon_cart.domain.spi.CartPersistencePort;
import com.pragma.emazon_cart.infrastructure.out.jpa.mapper.CartEntityMapper;
import com.pragma.emazon_cart.infrastructure.out.jpa.repository.CartRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CartJpaAdapter implements CartPersistencePort {

    private final CartRepository cartRepository;
    private final CartEntityMapper cartEntityMapper;

    @Override
    public void saveCart(Cart cart) {
        cartRepository.save(cartEntityMapper.toEntity(cart));
    }

    @Override
    public Optional<Cart> findCartByUserId(Integer userId) {
        return cartRepository.findByCartUserId(userId)
                .map(cartEntityMapper::toDomain);
    }
}
