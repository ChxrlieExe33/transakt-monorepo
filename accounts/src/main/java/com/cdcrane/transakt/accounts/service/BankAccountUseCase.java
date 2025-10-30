package com.cdcrane.transakt.accounts.service;

import com.cdcrane.transakt.accounts.dto.BankAccountOpenedResponse;

import java.util.UUID;

public interface BankAccountUseCase {

    BankAccountOpenedResponse openBankAccount(UUID customerId);
}
