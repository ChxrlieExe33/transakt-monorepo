package com.cdcrane.customers.service;

import com.cdcrane.customers.dto.EmailVerifiedResponse;
import com.cdcrane.customers.dto.RegisterCustomerRequest;
import com.cdcrane.customers.dto.SubmitVerificationCodeRequest;
import com.cdcrane.customers.entity.Customer;
import com.cdcrane.customers.event.CustomerRegisteredEvent;
import com.cdcrane.customers.event.CustomerVerifiedEvent;
import com.cdcrane.customers.exception.CustomerNotOldEnoughException;
import com.cdcrane.customers.exception.EmailTakenException;
import com.cdcrane.customers.exception.InvalidVerificationCodeException;
import com.cdcrane.customers.exception.ResourceNotFoundException;
import com.cdcrane.customers.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class CustomerService implements CustomerUseCase{

    private final CustomerRepository customerRepo;
    private final StreamBridge streamBridge;
    private final ApplicationEventPublisher localEventPublisher;
    private final SecurityUtils securityUtils;
    private final EmailUseCase emailUseCase;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, StreamBridge streamBridge, ApplicationEventPublisher applicationEventPublisher, SecurityUtils securityUtils, EmailUseCase emailUseCase) {
        this.customerRepo = customerRepository;
        this.streamBridge = streamBridge;
        this.localEventPublisher = applicationEventPublisher;
        this.securityUtils = securityUtils;
        this.emailUseCase = emailUseCase;
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

        int verificationCode = securityUtils.generateVerificationCode();

        Customer customer = Customer.builder()
                .firstName(customerData.firstName())
                .lastName(customerData.lastName())
                .email(customerData.email())
                .birthDate(customerData.birthDate())
                .address(customerData.address())
                .city(customerData.city())
                .jobTitle(customerData.jobTitle())
                .verified(false)
                .verificationCode(verificationCode)
                .build();

        Customer saved = customerRepo.save(customer);

        var event = new CustomerRegisteredEvent(saved.getCustomerId(), saved.getFirstName(), saved.getLastName(), saved.getEmail(), saved.getBirthDate(), saved.getAddress(), saved.getCity(), saved.getJobTitle(), saved.getVerificationCode());

        // Send the event through kafka.
        streamBridge.send("customerRegistered-out-0", event);
        localEventPublisher.publishEvent(event);

    }

    @Override
    @Transactional
    public EmailVerifiedResponse checkVerificationCode(SubmitVerificationCodeRequest request) {

        Customer customer = customerRepo.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for email " + request.email() + "."));

        if (!request.verificationCode().equals(customer.getVerificationCode())) {

            log.warn("Verification code {} does not match for customer {}", request.verificationCode(), customer.getEmail());

            // Reset code
            customer.setVerificationCode(securityUtils.generateVerificationCode());

            // Resend email
            emailUseCase.sendVerificationEmail(customer.getEmail(), customer.getFirstName(), customer.getVerificationCode());

            customerRepo.save(customer);

            throw new InvalidVerificationCodeException("Invalid verification code, another has been sent to " + customer.getEmail() + ".");

        }

        customer.setVerified(true);
        customer.setVerificationCode(null);
        customerRepo.save(customer);

        String temporaryPassword = securityUtils.generateTemporaryPassword();

        CustomerVerifiedEvent event = new CustomerVerifiedEvent(customer.getFirstName(), customer.getLastName(), customer.getEmail(), temporaryPassword);

        localEventPublisher.publishEvent(event);

        return new EmailVerifiedResponse("Your email is verified.", temporaryPassword);

    }


}
