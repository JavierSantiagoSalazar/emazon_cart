package com.pragma.emazon_cart.application.mappers;

import com.pragma.emazon_cart.application.dto.CartResponseDto;
import com.pragma.emazon_cart.domain.model.CartResponse;
import com.pragma.emazon_cart.domain.utils.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = Constants.SPRING_COMPONENT_MODEL,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)

public interface CartResponseDtoMapper {

    CartResponseDto toResponseDto(CartResponse article);

}
