package com.empresa.sistemacore.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI sistemaCoreApi() {
        return new OpenAPI().info(new Info().title("Sistema Core API").version("1"));
    }
}
