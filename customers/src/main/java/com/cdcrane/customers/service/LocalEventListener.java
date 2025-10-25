package com.cdcrane.customers.service;

import com.cdcrane.customers.event.CustomerRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@EnableAsync
@Component
public class LocalEventListener {

    private final KeycloakUserManagerService keycloakUserManagerService;

    public LocalEventListener(KeycloakUserManagerService keycloakUserManagerService) {
        this.keycloakUserManagerService = keycloakUserManagerService;
    }

    @Async
    @EventListener(CustomerRegisteredEvent.class)
    public void onCustomerRegistered(CustomerRegisteredEvent event) {

        this.keycloakUserManagerService.createUserAccount(event);

    }

}
