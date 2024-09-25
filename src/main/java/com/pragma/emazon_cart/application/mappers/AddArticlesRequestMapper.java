package com.pragma.emazon_cart.application.mappers;

import com.pragma.emazon_cart.application.dto.AddArticlesRequest;
import com.pragma.emazon_cart.domain.model.AddArticles;
import com.pragma.emazon_cart.domain.utils.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = Constants.SPRING_COMPONENT_MODEL,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AddArticlesRequestMapper {

    default AddArticles toDomain(List<AddArticlesRequest> addArticlesRequestList) {
        List<Integer> articleIds = mapArticleIds(addArticlesRequestList);
        List<Integer> articleAmounts = mapArticleAmounts(addArticlesRequestList);
        return new AddArticles(articleIds, articleAmounts);
    }

    default List<Integer> mapArticleIds(List<AddArticlesRequest> addArticlesRequestList) {
        return addArticlesRequestList.stream()
                .map(AddArticlesRequest::getArticleId)
                .toList();
    }

    default List<Integer> mapArticleAmounts(List<AddArticlesRequest> addArticlesRequestList) {
        return addArticlesRequestList.stream()
                .map(AddArticlesRequest::getArticleAmount)
                .toList();
    }

}
