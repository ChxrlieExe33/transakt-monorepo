package com.cdcrane.transakt.transactions.exception;

public class NotAuthorizedForTransferException extends RuntimeException{
    public NotAuthorizedForTransferException(String message) {
        super(message);
    }
}
