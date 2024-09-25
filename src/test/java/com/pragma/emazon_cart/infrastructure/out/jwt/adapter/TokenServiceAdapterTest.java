package com.pragma.emazon_cart.infrastructure.out.jwt.adapter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pragma.emazon_cart.infrastructure.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TokenServiceAdapterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private DecodedJWT decodedJWT;

    @Mock
    private Claim claim;

    @InjectMocks
    private TokenServiceAdapter tokenServiceAdapter;

    private final String jwtToken = "test-jwt-token";

    @BeforeEach
    void setUp() {

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getCredentials()).thenReturn(jwtToken);
    }

    @Test
    void givenValidToken_whenExtractUserIdFromTokenIsCalled_thenReturnsUserId() {

        when(jwtUtils.validateToken(jwtToken)).thenReturn(decodedJWT);
        when(jwtUtils.getSpecificClaim(decodedJWT, "userId")).thenReturn(claim);
        Integer expectedUserId = 123;
        when(claim.asInt()).thenReturn(expectedUserId);

        Integer userId = tokenServiceAdapter.extractUserIdFromToken();

        assertEquals(expectedUserId, userId);
        verify(jwtUtils).validateToken(jwtToken);
        verify(jwtUtils).getSpecificClaim(decodedJWT, "userId");
    }

    @Test
    void givenInvalidToken_whenExtractUserIdFromTokenIsCalled_thenThrowsJWTVerificationException() {

        when(jwtUtils.validateToken(jwtToken)).thenThrow(new JWTVerificationException("Invalid Token"));

        assertThrows(JWTVerificationException.class, () -> tokenServiceAdapter.extractUserIdFromToken());
        verify(jwtUtils).validateToken(jwtToken);
    }

}