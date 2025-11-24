package com.cdcrane.transakt.transactions.listener;

import com.cdcrane.transakt.transactions.event.AccountOpenedEvent;
import com.cdcrane.transakt.transactions.event.TransferCompleteEvent;
import com.cdcrane.transakt.transactions.event.TransferRejectedEvent;
import com.cdcrane.transakt.transactions.service.BankAccountProjectionUseCase;
import com.cdcrane.transakt.transactions.service.BankTransferUseCase;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class RemoteEventListener {

    @Bean
    public Consumer<AccountOpenedEvent> accountOpened(BankAccountProjectionUseCase bankAccountProjectionUseCase) {

        return bankAccountProjectionUseCase::saveNewAccountProjection;
    }

    @Bean
    public Consumer<TransferCompleteEvent> transferSuccess(BankTransferUseCase bankTransferUseCase, ApplicationEventPublisher localPublisher) {

        return event -> {
            bankTransferUseCase.handleTransferCompleted(event.transferId());
            localPublisher.publishEvent(event);
        };

    }

    @Bean
    public Consumer<TransferRejectedEvent> transferRejected(BankTransferUseCase bankTransferUseCase, ApplicationEventPublisher localPublisher) {

        return event -> {
            bankTransferUseCase.handleTransferRejected(event.transferId(), event.reason());
            localPublisher.publishEvent(event);
        };

    }
}
