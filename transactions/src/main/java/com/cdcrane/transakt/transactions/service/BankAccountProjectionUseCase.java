package com.cdcrane.transakt.transactions.service;

import com.cdcrane.transakt.transactions.event.AccountOpenedEvent;

public interface BankAccountProjectionUseCase {

    void saveNewAccountProjection(AccountOpenedEvent event);
}
