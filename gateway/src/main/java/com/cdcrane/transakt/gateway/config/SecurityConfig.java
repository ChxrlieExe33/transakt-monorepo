package com.cdcrane.transakt.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http.authorizeExchange(e -> e
                .pathMatchers(HttpMethod.POST, "/customers/api/v1").permitAll()
                .pathMatchers(HttpMethod.POST, "/customers/api/v1/verify").permitAll()
                .pathMatchers(HttpMethod.GET, "/customers/api/v1/hello").authenticated()
        );

        http.oauth2ResourceServer(spec -> spec.jwt(Customizer.withDefaults()));

        http.csrf(ServerHttpSecurity.CsrfSpec::disable);

        return http.build();
    }
}
