package com.daniela.creditflow.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI creditFlowOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CreditFlow API")
                        .description("REST API for credit management")
                        .version("1.0.0"));
    }
}

