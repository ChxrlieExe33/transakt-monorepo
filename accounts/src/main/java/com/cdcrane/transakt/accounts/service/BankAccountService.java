package com.cdcrane.transakt.accounts.service;

import com.cdcrane.transakt.accounts.dto.BankAccountOpenedResponse;
import com.cdcrane.transakt.accounts.dto.OpenBankAccountRequest;
import com.cdcrane.transakt.accounts.entity.BankAccount;
import com.cdcrane.transakt.accounts.entity.CustomerProjection;
import com.cdcrane.transakt.accounts.event.AccountOpenedEvent;
import com.cdcrane.transakt.accounts.event.CashDepositedEvent;
import com.cdcrane.transakt.accounts.exception.CannotOpenAnotherAccountException;
import com.cdcrane.transakt.accounts.exception.ResourceNotFoundException;
import com.cdcrane.transakt.accounts.repository.BankAccountRepository;
import com.cdcrane.transakt.accounts.repository.CustomerProjectionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class BankAccountService implements BankAccountUseCase{

    private final BankAccountRepository bankAccountRepository;
    private final CustomerProjectionRepository customerProjectionRepository;
    private final StreamBridge streamBridge;

    public BankAccountService(BankAccountRepository bankAccountRepository, CustomerProjectionRepository customerProjectionRepository, StreamBridge streamBridge) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerProjectionRepository = customerProjectionRepository;
        this.streamBridge = streamBridge;
    }

    @Override
    @Transactional
    public BankAccountOpenedResponse openBankAccount(UUID customerId, OpenBankAccountRequest data) {

        // Check that the customer exists by checking local customer projection.
        CustomerProjection projection = customerProjectionRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Accounts-svc customer projection with ID " + customerId + " not found."));

        // Check customer has been enabled
        if (!projection.getCustomerVerified()) {
            throw new CannotOpenAnotherAccountException("Customer with ID " + customerId + " has not been verified.");
        }

        // Check if the customer already has an account, and if so, are they allowed to create another.
        if (bankAccountRepository.existsByCustomerId(customerId) && !projection.getNewAccountCreationPermitted()) {
            throw new CannotOpenAnotherAccountException("Customer with ID " + customerId + " already has an account, creation of another requires manager approval");
        }

        BankAccount account = BankAccount.builder()
                .customerId(customerId)
                .accountName(data.accountName())
                .currentBalance(0.0)
                .enabled(true)
                .overdraftPermitted(false)
                .build();

        BankAccount saved = bankAccountRepository.save(account);

        log.info("Saved new bank account for customer {} with bank account ID {}.", customerId, saved.getAccountId());

        projection.setNewAccountCreationPermitted(false);
        customerProjectionRepository.save(projection);

        var event = new AccountOpenedEvent(saved.getAccountId(), saved.getCurrentBalance(), saved.getCustomerId());

        streamBridge.send("accountOpened-out-0", event);

        return new BankAccountOpenedResponse(account.getAccountId(), account.getCurrentBalance());

    }

    @Override
    @Transactional
    public void adjustBalanceFromCashDeposit(CashDepositedEvent event) {

        Optional<BankAccount> account = bankAccountRepository.findById(event.accountId());

        if(account.isEmpty()) {
            log.error("Bank account with ID {} not found. Could not adjust balance from deposit.", event.accountId());
            return;
        }

        BankAccount bankAccount = account.get();

        bankAccount.setCurrentBalance(bankAccount.getCurrentBalance() + event.amount());

        bankAccountRepository.save(bankAccount);

        log.info("Adjusted balance for bank account {} by {} due to cash deposit with ID {}", event.accountId(), event.amount(), event.cashDepositId());

    }

}
