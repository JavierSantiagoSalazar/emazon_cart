package com.pragma.emazon_cart.infrastructure.configuration.bean;

import com.pragma.emazon_cart.infrastructure.feing.FeignClientInterceptor;
import feign.Client;
import feign.httpclient.ApacheHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignCommonConfiguration {

    @Bean
    public FeignClientInterceptor feignClientInterceptor() {
        return new FeignClientInterceptor();
    }

    @Bean
    public Client feignClient() {
        return new ApacheHttpClient();
    }

}
