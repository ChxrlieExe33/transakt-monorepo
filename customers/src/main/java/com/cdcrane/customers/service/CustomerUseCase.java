package com.cdcrane.customers.service;

import com.cdcrane.customers.dto.EmailVerifiedResponse;
import com.cdcrane.customers.dto.RegisterCustomerRequest;
import com.cdcrane.customers.dto.SubmitVerificationCodeRequest;
import com.cdcrane.customers.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerUseCase {

    Page<Customer> getAllCustomers(Pageable pageable);

    void registerCustomer(RegisterCustomerRequest customerData);

    EmailVerifiedResponse checkVerificationCode(SubmitVerificationCodeRequest verificationCode);
}
