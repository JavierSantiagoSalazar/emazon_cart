package com.pragma.emazon_cart.infrastructure.out.jpa.repository;

import com.pragma.emazon_cart.infrastructure.out.jpa.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Integer> {

    Optional<CartEntity> findByCartUserId(Integer userId);

}
