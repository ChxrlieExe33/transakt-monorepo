package com.cdcrane.transakt.transactions.service;

import com.cdcrane.transakt.transactions.dto.CashDepositRequest;
import com.cdcrane.transakt.transactions.dto.CashWithdrawalRequest;
import com.cdcrane.transakt.transactions.entity.BankAccountProjection;
import com.cdcrane.transakt.transactions.entity.CashDeposit;
import com.cdcrane.transakt.transactions.entity.CashWithdrawal;
import com.cdcrane.transakt.transactions.event.CashDepositedEvent;
import com.cdcrane.transakt.transactions.event.CashWithdrawnEvent;
import com.cdcrane.transakt.transactions.exception.AccountNotFoundException;
import com.cdcrane.transakt.transactions.exception.NotAuthorizedForCashOperationException;
import com.cdcrane.transakt.transactions.exception.NotEnoughFundsException;
import com.cdcrane.transakt.transactions.repository.BankAccountProjectionRepository;
import com.cdcrane.transakt.transactions.repository.CashDepositRepository;
import com.cdcrane.transakt.transactions.repository.CashWithdrawalRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class CashOperationsService implements CashOperationsUseCase {

    private final BankAccountProjectionRepository bankAccountProjectionRepo;
    private final CashDepositRepository cashDepositRepo;
    private final StreamBridge streamBridge;
    private final CashWithdrawalRepository cashWithdrawalRepo;

    public CashOperationsService(BankAccountProjectionRepository bankAccountProjectionRepository, CashDepositRepository cashDepositRepository, StreamBridge streamBridge, CashWithdrawalRepository cashWithdrawalRepository) {
        this.bankAccountProjectionRepo = bankAccountProjectionRepository;
        this.cashDepositRepo = cashDepositRepository;
        this.streamBridge = streamBridge;
        this.cashWithdrawalRepo = cashWithdrawalRepository;
    }

    @Override
    @Transactional
    public void depositCash(CashDepositRequest data, UUID customerId) {

        BankAccountProjection accountProjection = bankAccountProjectionRepo.findById(data.accountId())
                .orElseThrow(() -> new AccountNotFoundException("Account " + data.accountId() + " not found, cannot deposit cash"));

        if (!accountProjection.getCustomerId().equals(customerId)) {
            throw new NotAuthorizedForCashOperationException("Account " + data.accountId() + " belongs to customer " + accountProjection.getCustomerId() + ", customer " + customerId + " is not authorized to deposit cash.");
        }

        CashDeposit deposit = CashDeposit.builder()
                .accountId(data.accountId())
                .amount(data.amount())
                .concept(data.concept())
                .build();


        accountProjection.setCurrentBalance(accountProjection.getCurrentBalance() + data.amount());

        var saved = cashDepositRepo.save(deposit);
        bankAccountProjectionRepo.save(accountProjection);

        log.info("Cash deposit for account {} with amount {} and concept '{}' saved.", data.accountId(), data.amount(), data.concept());

        var event = new CashDepositedEvent(saved.getCashDepositId(), saved.getAccountId(), saved.getAmount(), saved.getConcept());

        streamBridge.send("cashDeposited-out-0", event);

    }

    @Override
    public void withdrawCash(CashWithdrawalRequest data, UUID currentCustomerId) {

        BankAccountProjection accountProjection = bankAccountProjectionRepo.findById(data.accountId())
                .orElseThrow(() -> new AccountNotFoundException("Account " + data.accountId() + " not found, cannot withdraw cash."));

        if (!accountProjection.getCustomerId().equals(currentCustomerId)) {
            throw new NotAuthorizedForCashOperationException("Account " + data.accountId() + " belongs to customer " + accountProjection.getCustomerId() + ", customer " + currentCustomerId + " is not authorized to withdraw cash.");
        }

        if (accountProjection.getCurrentBalance() < data.amount()) {
            throw new NotEnoughFundsException("Account " + data.accountId() + " does not have enough funds to withdraw " + data.amount() + " for concept " + data.concept() + ". If this seems incorrect, please contact your local branch.");
        }

        // TODO: Get cash withdrawals on this account within the past 24h, if this brings the total over 1000€, deny it.

        accountProjection.setCurrentBalance(accountProjection.getCurrentBalance() - data.amount());

        CashWithdrawal withdrawal = CashWithdrawal.builder()
                .concept(data.concept())
                .accountId(data.accountId())
                .amount(data.amount())
                .build();

        CashWithdrawal saved = cashWithdrawalRepo.save(withdrawal);
        bankAccountProjectionRepo.save(accountProjection);

        CashWithdrawnEvent event = new CashWithdrawnEvent(saved.getCashWithdrawalId(), saved.getAccountId(), saved.getAmount(), saved.getConcept());

        log.info("Cash withdrawal for account {} with amount {} and concept '{}' saved.", data.accountId(), data.amount(), data.concept());

        streamBridge.send("cashWithdrawn-out-0", event);

    }
}
