package com.cdcrane.transakt.gateway.routes;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransaktRoutes {

    @Bean
    public RouteLocator customerRoutes(RouteLocatorBuilder builder) {

        return builder.routes()
                .route("customers-service", r -> r
                    .path("/customers/**")
                    .filters(f -> f.stripPrefix(1))
                    .uri("http://localhost:8080"))
                .build();

    }
}
