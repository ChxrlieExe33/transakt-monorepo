package com.cdcrane.customers.exception;

public class InvalidVerificationCodeException extends RuntimeException{
    public InvalidVerificationCodeException(String message) {
        super(message);
    }
}
