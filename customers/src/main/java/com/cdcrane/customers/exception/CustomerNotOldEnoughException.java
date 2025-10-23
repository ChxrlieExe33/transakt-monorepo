package com.cdcrane.customers.exception;

public class CustomerNotOldEnoughException extends RuntimeException {
    public CustomerNotOldEnoughException(String message) {
        super(message);
    }
}
