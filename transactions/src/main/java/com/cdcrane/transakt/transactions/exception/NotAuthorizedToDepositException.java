package com.cdcrane.transakt.transactions.exception;

public class NotAuthorizedToDepositException extends RuntimeException{
    public NotAuthorizedToDepositException(String message) {
        super(message);
    }
}
