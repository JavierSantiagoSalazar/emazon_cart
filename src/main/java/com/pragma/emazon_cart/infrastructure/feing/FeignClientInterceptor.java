package com.pragma.emazon_cart.infrastructure.feing;

import com.pragma.emazon_cart.domain.exceptions.JwtIsEmptyException;
import com.pragma.emazon_cart.domain.exceptions.UnauthorizedException;
import com.pragma.emazon_cart.domain.utils.Constants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getCredentials() != null) {

            String token = authentication.getCredentials().toString();

            if (token.isEmpty()) {
                throw new JwtIsEmptyException(Constants.JWT_IS_EMPTY_ERROR);
            }

            requestTemplate.header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN_PREFIX + token);

        } else {
            throw new UnauthorizedException(Constants.UNAUTHORIZED_ERROR);
        }
    }

}