package com.cdcrane.transakt.transactions.service;

import com.cdcrane.transakt.transactions.event.AccountOpenedEvent;

import java.util.UUID;

public interface BankAccountProjectionUseCase {

    void saveNewAccountProjection(AccountOpenedEvent event);

    UUID getCustomerWhoOwnsAccount(UUID accountId);
}
