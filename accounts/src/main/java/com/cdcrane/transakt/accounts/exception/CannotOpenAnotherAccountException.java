package com.cdcrane.transakt.accounts.exception;

public class CannotOpenAnotherAccountException extends RuntimeException{
    public CannotOpenAnotherAccountException(String message) {
        super(message);
    }
}
