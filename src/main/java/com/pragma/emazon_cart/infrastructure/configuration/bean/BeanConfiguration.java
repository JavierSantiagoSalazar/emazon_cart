package com.pragma.emazon_cart.infrastructure.configuration.bean;

import com.pragma.emazon_cart.domain.api.CartServicePort;
import com.pragma.emazon_cart.domain.spi.CartPersistencePort;
import com.pragma.emazon_cart.domain.spi.FeignClientPort;
import com.pragma.emazon_cart.domain.spi.TokenServicePort;
import com.pragma.emazon_cart.domain.usecase.CartUseCase;
import com.pragma.emazon_cart.infrastructure.configuration.security.exceptionhandler.CustomAuthenticationEntryPoint;
import com.pragma.emazon_cart.infrastructure.feing.stock.StockFeignClient;
import com.pragma.emazon_cart.infrastructure.feing.transaction.TransactionFeignClient;
import com.pragma.emazon_cart.infrastructure.out.feing.adapter.FeignClientAdapter;
import com.pragma.emazon_cart.infrastructure.out.feing.mapper.ArticleResponseMapper;
import com.pragma.emazon_cart.infrastructure.out.jpa.adapter.CartJpaAdapter;
import com.pragma.emazon_cart.infrastructure.out.jpa.mapper.CartEntityMapper;
import com.pragma.emazon_cart.infrastructure.out.jpa.repository.CartRepository;
import com.pragma.emazon_cart.infrastructure.out.jwt.adapter.TokenServiceAdapter;
import com.pragma.emazon_cart.infrastructure.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final CartRepository cartRepository;
    private final CartEntityMapper cartEntityMapper;

    private final StockFeignClient stockFeignClient;
    private final TransactionFeignClient transactionFeignClient;
    private final ArticleResponseMapper articleResponseMapper;

    private final JwtUtils jwtUtils;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public CartPersistencePort cartPersistencePort() {
        return new CartJpaAdapter(cartRepository, cartEntityMapper);
    }

    @Bean
    public FeignClientPort feignClientPort() {
        return new FeignClientAdapter(stockFeignClient, transactionFeignClient, articleResponseMapper);
    }

    @Bean
    public TokenServicePort tokenServicePort() {
        return new TokenServiceAdapter(jwtUtils);
    }

    @Bean
    public CartServicePort cartServicePort() {
        return new CartUseCase(cartPersistencePort(), tokenServicePort(), feignClientPort());
    }

}
