package com.pragma.emazon_cart.infrastructure.configuration.documentation;

import com.pragma.emazon_cart.domain.utils.Constants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi(
            @Value(Constants.OPEN_API_APP_DESCRIPTION) String appDescription,
            @Value(Constants.OPEN_API_APP_VERSION) String appVersion
    ) {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title(Constants.OPEN_API_TITLE)
                        .version(appVersion)
                        .description(appDescription)
                        .termsOfService(Constants.OPEN_API_TERMS)
                        .license(new License().name(Constants.OPEN_API_LICENCE_NAME)
                                .url(Constants.OPEN_API_LICENCE_URL))
                );
    }

}
