package com.cdcrane.transakt.transactions.exception;

public class NotAuthorisedToQueryTransactionsException extends RuntimeException{
    public NotAuthorisedToQueryTransactionsException(String message) {
        super(message);
    }
}
