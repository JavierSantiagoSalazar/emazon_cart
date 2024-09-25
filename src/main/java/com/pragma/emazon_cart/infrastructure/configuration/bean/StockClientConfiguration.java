package com.pragma.emazon_cart.infrastructure.configuration.bean;

import com.pragma.emazon_cart.infrastructure.feing.stock.StockErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StockClientConfiguration {

    @Bean
    public ErrorDecoder stockErrorDecoder() {
        return new StockErrorDecoder();
    }

}
