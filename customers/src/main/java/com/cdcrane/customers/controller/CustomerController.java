package com.cdcrane.customers.controller;

import com.cdcrane.customers.dto.EmailVerifiedResponse;
import com.cdcrane.customers.dto.RegisterCustomerRequest;
import com.cdcrane.customers.dto.SubmitVerificationCodeRequest;
import com.cdcrane.customers.service.CustomerUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerUseCase customerUseCase;

    public CustomerController(CustomerUseCase customerUseCase) {
        this.customerUseCase = customerUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> registerCustomer(@RequestBody RegisterCustomerRequest customerData) {

        customerUseCase.registerCustomer(customerData);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/verify")
    public ResponseEntity<EmailVerifiedResponse> checkVerificationCode(@Valid @RequestBody SubmitVerificationCodeRequest request) {

        EmailVerifiedResponse response = customerUseCase.checkVerificationCode(request);

        return ResponseEntity.ok(response);

    }
}
