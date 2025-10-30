package com.cdcrane.transakt.accounts.service;

import com.cdcrane.transakt.accounts.event.CustomerRegisteredEvent;

import java.util.UUID;

public interface CustomerProjectionUseCase {

    void saveNewCustomer(CustomerRegisteredEvent event);

    void enableCustomer(UUID customerId);
}
