package com.cdcrane.customers.service;

import com.cdcrane.customers.dto.RegisterCustomerRequest;
import com.cdcrane.customers.entity.Customer;
import com.cdcrane.customers.event.CustomerRegisteredEvent;
import com.cdcrane.customers.exception.CustomerNotOldEnoughException;
import com.cdcrane.customers.exception.EmailTakenException;
import com.cdcrane.customers.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CustomerService implements CustomerUseCase{

    private final CustomerRepository customerRepo;
    private final StreamBridge streamBridge;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, StreamBridge streamBridge) {
        this.customerRepo = customerRepository;
        this.streamBridge = streamBridge;
    }


    /**
     * Initial registration for a customer in the application.
     * Requires an email verification step after.
     * @param customerData The request containing customer information.
     */
    @Override
    @Transactional
    public void registerCustomer(RegisterCustomerRequest customerData) {

        if (customerRepo.existsByEmail(customerData.email())) {
            throw new EmailTakenException("Email " + customerData.email() + " is already taken.");
        }

        var today = LocalDate.now();

        if (customerData.birthDate().plusYears(18).isAfter(today)) {

            throw new CustomerNotOldEnoughException("Customer " + customerData.email() + " is not old enough.");

        }

        Customer customer = Customer.builder()
                .firstName(customerData.firstName())
                .lastName(customerData.lastName())
                .email(customerData.email())
                .birthDate(customerData.birthDate())
                .address(customerData.address())
                .city(customerData.city())
                .jobTitle(customerData.jobTitle())
                .emailVerified(false)
                .build();

        Customer saved = customerRepo.save(customer);

        var event = new CustomerRegisteredEvent(saved.getCustomerId(), saved.getFirstName(), saved.getLastName(), saved.getEmail(), saved.getBirthDate(), saved.getAddress(), saved.getCity(), saved.getJobTitle());

        // Send the event through kafka.
        streamBridge.send("customerRegistered-out-0", event);

    }


}
