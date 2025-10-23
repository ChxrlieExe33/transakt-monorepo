package com.cdcrane.customers.controller;

import com.cdcrane.customers.dto.RegisterCustomerRequest;
import com.cdcrane.customers.service.CustomerUseCase;
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

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from customers service!");
    }

    @PostMapping
    public ResponseEntity<Void> registerCustomer(@RequestBody RegisterCustomerRequest customerData) {

        customerUseCase.registerCustomer(customerData);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
