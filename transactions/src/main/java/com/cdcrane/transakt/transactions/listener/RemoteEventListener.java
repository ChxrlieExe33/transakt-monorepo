package com.cdcrane.transakt.transactions.listener;

import com.cdcrane.transakt.transactions.event.AccountOpenedEvent;
import com.cdcrane.transakt.transactions.service.BankAccountProjectionUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class RemoteEventListener {

    @Bean
    public Consumer<AccountOpenedEvent> accountOpened(BankAccountProjectionUseCase bankAccountProjectionUseCase) {

        return bankAccountProjectionUseCase::saveNewAccountProjection;
    }
}
