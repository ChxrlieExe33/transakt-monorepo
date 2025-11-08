package com.cdcrane.transakt.transactions.service;

import com.cdcrane.transakt.transactions.dto.CashDepositRequest;
import com.cdcrane.transakt.transactions.entity.BankAccountProjection;
import com.cdcrane.transakt.transactions.entity.CashDeposit;
import com.cdcrane.transakt.transactions.event.CashDepositedEvent;
import com.cdcrane.transakt.transactions.exception.AccountNotFoundException;
import com.cdcrane.transakt.transactions.exception.NotAuthorizedToDepositException;
import com.cdcrane.transakt.transactions.repository.BankAccountProjectionRepository;
import com.cdcrane.transakt.transactions.repository.CashDepositRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class CashDepositService implements CashDepositUseCase {

    private final BankAccountProjectionRepository bankAccountProjectionRepo;
    private final CashDepositRepository cashDepositRepo;
    private final StreamBridge streamBridge;

    public CashDepositService(BankAccountProjectionRepository bankAccountProjectionRepository, CashDepositRepository cashDepositRepository, StreamBridge streamBridge) {
        this.bankAccountProjectionRepo = bankAccountProjectionRepository;
        this.cashDepositRepo = cashDepositRepository;
        this.streamBridge = streamBridge;
    }

    @Override
    @Transactional
    public void depositCash(CashDepositRequest data, UUID customerId) {

        BankAccountProjection accountProjection = bankAccountProjectionRepo.findById(data.accountId())
                .orElseThrow(() -> new AccountNotFoundException("Account " + data.accountId() + " not found, cannot deposit cash"));

        if (!accountProjection.getCustomerId().equals(customerId)) {
            throw new NotAuthorizedToDepositException("Account " + data.accountId() + " belongs to customer " + accountProjection.getCustomerId() + ", customer " + customerId + " is not authorized to deposit cash.");
        }

        CashDeposit deposit = CashDeposit.builder()
                .accountId(data.accountId())
                .amount(data.amount())
                .concept(data.concept())
                .build();

        var saved = cashDepositRepo.save(deposit);

        log.info("Cash deposit for account {} with amount {} and concept '{}' saved.", data.accountId(), data.amount(), data.concept());

        var event = new CashDepositedEvent(saved.getCashDepositId(), saved.getAccountId(), saved.getAmount(), saved.getConcept());

        streamBridge.send("cashDeposited-out-0", event);

    }
}
