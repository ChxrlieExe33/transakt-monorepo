package com.cdcrane.transakt.transactions.exception;

public class NotAuthorizedForCashOperationException extends RuntimeException{
    public NotAuthorizedForCashOperationException(String message) {
        super(message);
    }
}
