package com.example.apigatway;

import com.example.apigatway.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                // CHATBOT API (PUBLIC - Sans authentification JWT)
                .route("chatbot-public", r -> r
                        .path("/api/chatbot/**")
                        // Pas de filtre JWT - route publique
                        .uri("http://localhost:8082"))

                // AUTH-SERVICE → On laisse /api/auth/** sans stripPrefix
                .route("auth_service", r -> r
                        .path("/api/auth/**")
                        .filters(f -> f
                                .filter(jwtFilter.apply(new JwtAuthenticationFilter.Config()))
                        )
                        .uri("http://localhost:8087"))

                // CABINET-SERVICE → On laisse /cabinets/** sans stripPrefix
                .route("cabinet-service", r -> r
                        .path("/cabinets/**")
                        .filters(f -> f
                                .filter(jwtFilter.apply(new JwtAuthenticationFilter.Config()))
                        )
                        .uri("http://localhost:8082"))
                .route("patient-service", r -> r
                        .path("/patients/**")
                        .filters(f -> f
                                .filter(jwtFilter.apply(new JwtAuthenticationFilter.Config()))
                        )
                        .uri("http://localhost:8102"))
                .route("facture-service", r -> r
                        .path("/factures/**")
                        .filters(f -> f
                                .filter(jwtFilter.apply(new JwtAuthenticationFilter.Config()))
                        )
                        .uri("http://localhost:8083"))
                .route("rendezVous-service", r -> r
                        .path("/rendezVous/**")
                        .filters(f -> f
                                .filter(jwtFilter.apply(new JwtAuthenticationFilter.Config()))
                        )
                        .uri("http://localhost:8084"))
                .route("consultation-service", r -> r
                        .path("/consultations/**")
                        .filters(f -> f
                                .filter(jwtFilter.apply(new JwtAuthenticationFilter.Config()))
                        )
                        .uri("http://localhost:8103"))
                .build();
    }

    // Configuration CORS réactive (correcte pour Gateway)
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
        config.setAllowedHeaders(Arrays.asList(
                "Origin",
                "Content-Type",
                "Accept",
                "Authorization",
                "X-Requested-With",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setMaxAge(3600L); // Cache preflight pour 1 heure

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}