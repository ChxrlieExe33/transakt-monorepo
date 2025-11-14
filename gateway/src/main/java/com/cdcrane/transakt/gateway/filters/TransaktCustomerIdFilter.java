package com.cdcrane.transakt.gateway.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class TransaktCustomerIdFilter implements GlobalFilter, Ordered {

    private final String HEADER_NAME = "Transakt-Customer-Id";


    private final List<String> publicUris;

    public TransaktCustomerIdFilter(@Value("${gateway.public-paths}") List<String> publicUris) {
        this.publicUris = publicUris;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        var withUserProvidedHeaderRemoved = exchange.getRequest().mutate()
                .headers(h -> h.remove(HEADER_NAME))
                .build();

        var mutatedExchange = exchange.mutate().request(withUserProvidedHeaderRemoved).build();

        String requestPath = mutatedExchange.getRequest().getPath().value();

        boolean isPublicUri = publicUris.stream().anyMatch(requestPath::matches);

        if (isPublicUri) {
            return chain.filter(mutatedExchange);
        }

        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(auth -> auth instanceof JwtAuthenticationToken)
                .map(auth -> ((JwtAuthenticationToken) auth).getToken())
                .cast(Jwt.class)
                .flatMap(jwt -> {

                    String customerId = jwt.getClaimAsString("customerId");

                    var requestBuilder = mutatedExchange.getRequest().mutate();

                    if (customerId != null) {
                        requestBuilder.header(HEADER_NAME, customerId);
                    }

                    return chain.filter(mutatedExchange.mutate().request(requestBuilder.build()).build());

                })
                .switchIfEmpty(chain.filter(mutatedExchange));

    }

    @Override
    public int getOrder() {
        return -10;
    }

}
