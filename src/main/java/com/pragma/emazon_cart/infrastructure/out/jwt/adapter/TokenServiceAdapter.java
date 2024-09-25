package com.pragma.emazon_cart.infrastructure.out.jwt.adapter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.pragma.emazon_cart.domain.spi.TokenServicePort;
import com.pragma.emazon_cart.infrastructure.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.pragma.emazon_cart.domain.utils.Constants.CLAIM_USER_ID;

@RequiredArgsConstructor
public class TokenServiceAdapter implements TokenServicePort {

    private final JwtUtils jwtUtils;

    @Override
    public Integer extractUserIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String jwtToken = (String) authentication.getCredentials();

        DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);
        return jwtUtils.getSpecificClaim(decodedJWT, CLAIM_USER_ID).asInt();
    }
}
