package com.example.apigatway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    @Autowired
    private JwtUtils jwtUtils;

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            // Chemins publics : login, register, actuator, swagger, etc.
            if (path.startsWith("/api/auth/login") ||
                    path.startsWith("/actuator") ||
                    path.contains("swagger") ||
                    path.contains("v3/api-docs")) {
                return chain.filter(exchange);
            }

            // Vérifier le header Authorization
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "Missing Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Invalid Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            if (!jwtUtils.isValid(token)) {
                return onError(exchange, "Invalid or expired JWT token", HttpStatus.UNAUTHORIZED);
            }

            String username = jwtUtils.getUsername(token);
            String role = jwtUtils.getRole(token);

            // Forward des infos aux microservices
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-Auth-Username", username)
                    .header("X-Auth-Role", role != null ? role : "USER")
                    .header("X-Auth-Authorities", role != null ? "ROLE_" + role : "ROLE_USER")
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().set(HttpHeaders.CONTENT_TYPE, "application/json");
        String json = "{\"error\":\"" + message + "\"}";
        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(json.getBytes(StandardCharsets.UTF_8))));
    }

    public static class Config {
        // Pour ajouter des propriétés plus tard si besoin (ex: ignorePaths)
    }
}