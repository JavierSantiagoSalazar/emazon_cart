package com.pragma.emazon_cart.infrastructure.configuration.bean;

import com.pragma.emazon_cart.infrastructure.feing.transaction.TransactionErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionClientConfiguration {

    @Bean
    public ErrorDecoder transactionErrorDecoder() {
        return new TransactionErrorDecoder();
    }

}
