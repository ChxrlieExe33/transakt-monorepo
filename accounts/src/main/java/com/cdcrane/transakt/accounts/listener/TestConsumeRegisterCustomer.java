package com.cdcrane.transakt.accounts.listener;

import com.cdcrane.transakt.accounts.event.CustomerRegisteredEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class TestConsumeRegisterCustomer {

    @Bean(name = "customerRegistered")
    public Consumer<CustomerRegisteredEvent> customerRegistered() {

        return event -> {

            System.out.println("Customer registered event consumed");
            System.out.println("Email " + event.email());
            System.out.println("First name " + event.firstName());

        };

    }
}
