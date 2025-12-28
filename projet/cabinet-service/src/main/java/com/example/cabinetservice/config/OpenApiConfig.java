package com.example.cabinetservice.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI cabinetOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Cabinet Service API")
                .version("v1")
                .description("API pour gestion des cabinets et abonnements"));
    }
}