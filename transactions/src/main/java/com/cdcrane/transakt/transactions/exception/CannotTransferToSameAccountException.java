package com.cdcrane.transakt.transactions.exception;

public class CannotTransferToSameAccountException extends RuntimeException{
    public CannotTransferToSameAccountException(String message) {
        super(message);
    }
}
