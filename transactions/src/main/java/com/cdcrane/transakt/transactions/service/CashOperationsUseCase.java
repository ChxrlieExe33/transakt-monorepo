package com.cdcrane.transakt.transactions.service;

import com.cdcrane.transakt.transactions.dto.CashDepositRequest;
import com.cdcrane.transakt.transactions.dto.CashWithdrawalRequest;

import java.util.UUID;

public interface CashOperationsUseCase {

    void depositCash(CashDepositRequest data, UUID customerId);

    void withdrawCash(CashWithdrawalRequest data, UUID currentCustomerId);
}
