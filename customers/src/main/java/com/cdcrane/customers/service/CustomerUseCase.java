package com.cdcrane.customers.service;

import com.cdcrane.customers.dto.EmailVerifiedResponse;
import com.cdcrane.customers.dto.RegisterCustomerRequest;
import com.cdcrane.customers.dto.SubmitVerificationCodeRequest;

public interface CustomerUseCase {

    void registerCustomer(RegisterCustomerRequest customerData);

    EmailVerifiedResponse checkVerificationCode(SubmitVerificationCodeRequest verificationCode);
}
