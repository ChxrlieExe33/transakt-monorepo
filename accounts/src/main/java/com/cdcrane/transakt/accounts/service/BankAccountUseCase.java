package com.cdcrane.transakt.accounts.service;

import com.cdcrane.transakt.accounts.dto.BankAccountOpenedResponse;
import com.cdcrane.transakt.accounts.dto.OpenBankAccountRequest;
import com.cdcrane.transakt.accounts.event.CashDepositedEvent;
import com.cdcrane.transakt.accounts.event.CashWithdrawnEvent;

import java.util.UUID;

public interface BankAccountUseCase {

    BankAccountOpenedResponse openBankAccount(UUID customerId, OpenBankAccountRequest data);

    void adjustBalanceFromCashDeposit(CashDepositedEvent event);

    void adjustBalanceFromCashWithdrawal(CashWithdrawnEvent event);
}
