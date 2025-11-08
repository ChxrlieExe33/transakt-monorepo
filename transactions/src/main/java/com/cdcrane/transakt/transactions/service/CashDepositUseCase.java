package com.cdcrane.transakt.transactions.service;

import com.cdcrane.transakt.transactions.dto.CashDepositRequest;

import java.util.UUID;

public interface CashDepositUseCase {

    void depositCash(CashDepositRequest data, UUID customerId);
}
