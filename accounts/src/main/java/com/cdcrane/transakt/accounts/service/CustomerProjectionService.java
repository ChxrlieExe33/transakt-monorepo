package com.cdcrane.transakt.accounts.service;

import com.cdcrane.transakt.accounts.entity.CustomerProjection;
import com.cdcrane.transakt.accounts.event.CustomerRegisteredEvent;
import com.cdcrane.transakt.accounts.repository.CustomerProjectionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CustomerProjectionService implements CustomerProjectionUseCase{

    private final CustomerProjectionRepository customerProjectionRepository;

    public CustomerProjectionService(CustomerProjectionRepository customerProjectionRepository) {
        this.customerProjectionRepository = customerProjectionRepository;
    }

    @Override
    @Transactional
    public void saveNewCustomer(CustomerRegisteredEvent event) {

        CustomerProjection projection = CustomerProjection.builder()
                .customerId(event.customerId())
                .newAccountCreationPermitted(true)
                .customerVerified(false)
                .build();

        customerProjectionRepository.save(projection);

        log.info("Saved new customer projection for customer {}.", event.customerId());

    }

    @Override
    @Transactional
    public void enableCustomer(UUID customerId) {

        Optional<CustomerProjection> projection = customerProjectionRepository.findById(customerId);

        if (projection.isEmpty()) {
            log.error("Customer projection with ID {} not found.", customerId);
            return;
        }

        CustomerProjection customerProjection = projection.get();

        customerProjection.setCustomerVerified(true);

        customerProjectionRepository.save(customerProjection);

        log.info("Enabled customer projection for customer {}.", customerId);

    }


}
