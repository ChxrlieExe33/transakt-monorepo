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
                    .uri("lb://customers"))
                .build();

    }

    @Bean
    public RouteLocator accountsRoutes(RouteLocatorBuilder builder) {

        return builder.routes()
                .route("accounts-service", r -> r
                        .path("/accounts/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://accounts")
                ).build();

    }

    @Bean
    public RouteLocator transactionsRoutes(RouteLocatorBuilder builder) {

        return builder.routes()
                .route("transactions-service", r -> r
                        .path("/transactions/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://transactions")
                ).build();

    }
}
