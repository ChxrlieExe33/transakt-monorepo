package com.cdcrane.customers.service;

import com.cdcrane.customers.dto.RegisterCustomerRequest;

public interface CustomerUseCase {

    void registerCustomer(RegisterCustomerRequest customerData);
}
