package com.pragma.emazon_cart.infrastructure.out.jpa.entity;

import com.pragma.emazon_cart.infrastructure.out.jpa.mapper.IntegerListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Integer cartId;

    @Column(name = "cart_user_id", nullable = false, unique = true)
    private Integer cartUserId;

    @Column(name = "car_article_ids", nullable = false)
    @Convert(converter = IntegerListConverter.class)
    private List<Integer> cartArticleList;

    @Column(name = "cart_amounts", nullable = false)
    @Convert(converter = IntegerListConverter.class)
    private List<Integer> cartAmountList;

    @Column(name = "cart_last_update_date", nullable = false)
    private LocalDate cartLastUpdateDate;

    @Column(name = "cart_creation_date", nullable = false)
    private LocalDate cartCreationDate;

}
