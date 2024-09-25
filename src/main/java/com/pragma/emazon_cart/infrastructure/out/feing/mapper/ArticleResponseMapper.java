package com.pragma.emazon_cart.infrastructure.out.feing.mapper;

import com.pragma.emazon_cart.application.dto.stock.ArticleResponse;
import com.pragma.emazon_cart.domain.model.stock.Article;
import com.pragma.emazon_cart.domain.utils.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = Constants.SPRING_COMPONENT_MODEL,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ArticleResponseMapper {

    List<Article> toDomain(List<ArticleResponse> articleResponseList);

}
