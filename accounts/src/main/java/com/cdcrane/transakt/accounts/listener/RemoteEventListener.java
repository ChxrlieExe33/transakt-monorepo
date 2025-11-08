package com.cdcrane.transakt.accounts.listener;

import com.cdcrane.transakt.accounts.event.CashDepositedEvent;
import com.cdcrane.transakt.accounts.event.CustomerRegisteredEvent;
import com.cdcrane.transakt.accounts.event.CustomerVerifiedEvent;
import com.cdcrane.transakt.accounts.service.BankAccountUseCase;
import com.cdcrane.transakt.accounts.service.CustomerProjectionUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class RemoteEventListener {

    @Bean
    public Consumer<CustomerRegisteredEvent> customerRegistered(CustomerProjectionUseCase customerProjectionUseCase) {

        return customerProjectionUseCase::saveNewCustomer;

    }

    @Bean
    public Consumer<CustomerVerifiedEvent> customerVerified(CustomerProjectionUseCase customerProjectionUseCase) {

        return event -> {

            customerProjectionUseCase.enableCustomer(event.customerId());

        };

    }

    @Bean
    public Consumer<CashDepositedEvent> cashDeposited(BankAccountUseCase bankAccountUseCase) {

        return bankAccountUseCase::adjustBalanceFromCashDeposit;

    }

}
