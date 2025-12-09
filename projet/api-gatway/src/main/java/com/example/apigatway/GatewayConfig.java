package com.example.apigatway;

import com.example.apigatway.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                // AUTH-SERVICE → ON NE RETIRE RIEN
                .route("auth_service", r -> r.path("/api/auth/**")
                        .filters(f -> f
                                .stripPrefix(0)
                                .filter(jwtFilter.apply(new JwtAuthenticationFilter.Config())))

                        .uri("http://localhost:8080"))   // ← PAS de stripPrefix !

                // CABINET-SERVICE → on retire "cabinets"
                .route("cabinet-service", r -> r.path("/cabinets/**")
                        .filters(f -> f
                                .stripPrefix(0)
                                .filter(jwtFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8082"))
                .build();
    }
}