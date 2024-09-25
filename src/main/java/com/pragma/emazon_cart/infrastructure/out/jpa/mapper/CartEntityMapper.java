package com.pragma.emazon_cart.infrastructure.out.jpa.mapper;

import com.pragma.emazon_cart.domain.model.Cart;
import com.pragma.emazon_cart.domain.model.stock.Article;
import com.pragma.emazon_cart.domain.utils.Constants;
import com.pragma.emazon_cart.infrastructure.out.jpa.entity.CartEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = Constants.SPRING_COMPONENT_MODEL,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CartEntityMapper {


    @Mapping(source = "cart.cartArticleList", target = "cartArticleList", qualifiedByName = "mapArticlesToIds")
    CartEntity toEntity(Cart cart);

    @Mapping(source = "cartEntity.cartArticleList", target = "cartArticleList", qualifiedByName = "mapIdsToArticles")
    Cart toDomain(CartEntity cartEntity);

    @Named("mapArticlesToIds")
    default List<Integer> mapArticlesToIds(List<Article> articles) {
        return articles.stream()
                .map(Article::getArticleId)
                .toList();
    }

    @Named("mapIdsToArticles")
    default List<Article> mapIdsToArticles(List<Integer> articleIds) {
        return articleIds.stream()
                .map(id -> new Article(
                        id,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                        )
                )
                .toList();
    }

}
