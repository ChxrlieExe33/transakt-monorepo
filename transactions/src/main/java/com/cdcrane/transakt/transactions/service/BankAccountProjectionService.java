package com.cdcrane.transakt.transactions.service;

import com.cdcrane.transakt.transactions.entity.BankAccountProjection;
import com.cdcrane.transakt.transactions.event.AccountOpenedEvent;
import com.cdcrane.transakt.transactions.repository.BankAccountProjectionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BankAccountProjectionService implements BankAccountProjectionUseCase {

    private final BankAccountProjectionRepository projectionRepo;

    public BankAccountProjectionService(BankAccountProjectionRepository bankAccountProjectionRepository) {
        this.projectionRepo = bankAccountProjectionRepository;
    }

    @Override
    @Transactional
    public void saveNewAccountProjection(AccountOpenedEvent event) {

        if (projectionRepo.existsByAccountId(event.accountId())) {
            log.error("Account {} already exists in the local projections.", event.accountId());
            return;
        }

        BankAccountProjection projection = BankAccountProjection.builder()
                .accountId(event.accountId())
                .currentBalance(event.balance())
                .customerId(event.customerId())
                .build();

        projectionRepo.save(projection);

        log.info("New account projection saved for account {} with customer ID {} and balance {}.", event.accountId(), event.customerId(), event.balance());

    }

}
