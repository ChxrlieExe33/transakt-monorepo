package com.cdcrane.transakt.transactions.service;

import com.cdcrane.transakt.transactions.dto.BankTransferRequest;

import java.util.UUID;

public interface BankTransferUseCase {

    void transferMoney(BankTransferRequest request, UUID currentCustomerId);

    void handleTransferRejected(UUID transferId, String reason);

    void handleTransferCompleted(UUID transferId);
}
