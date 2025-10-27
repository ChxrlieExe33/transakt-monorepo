package com.cdcrane.customers.listener;

import com.cdcrane.customers.event.CustomerRegisteredEvent;
import com.cdcrane.customers.event.CustomerVerifiedEvent;
import com.cdcrane.customers.service.EmailUseCase;
import com.cdcrane.customers.service.KeycloakUserManagerService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@EnableAsync
@Component
public class LocalEventListener {

    private final KeycloakUserManagerService keycloakUserManagerService;
    private final EmailUseCase emailUseCase;


    public LocalEventListener(KeycloakUserManagerService keycloakUserManagerService, EmailUseCase emailUseCase) {
        this.keycloakUserManagerService = keycloakUserManagerService;
        this.emailUseCase = emailUseCase;
    }

    @Async
    @EventListener(CustomerRegisteredEvent.class)
    public void onCustomerRegistered(CustomerRegisteredEvent event) {

        emailUseCase.sendVerificationEmail(event.email(), event.firstName(), event.verificationCode());

    }

    @Async
    @EventListener(CustomerVerifiedEvent.class)
    public void onCustomerVerified(CustomerVerifiedEvent event) {

        this.keycloakUserManagerService.createUserAccount(event);

    }

}
