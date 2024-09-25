package com.pragma.emazon_cart.infrastructure.feing.transaction;

import com.pragma.emazon_cart.domain.utils.Constants;
import com.pragma.emazon_cart.infrastructure.configuration.bean.FeignCommonConfiguration;
import com.pragma.emazon_cart.infrastructure.configuration.bean.TransactionClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(
        name = Constants.TRANSACTION_MICROSERVICE_NAME,
        url = "${emazon_transaction.url}",
        configuration = { FeignCommonConfiguration.class, TransactionClientConfiguration.class }
)
public interface TransactionFeignClient {

    @GetMapping(value = "/")
    List<LocalDate> getRestockDate(@RequestParam List<Integer> articleIdList);

}
