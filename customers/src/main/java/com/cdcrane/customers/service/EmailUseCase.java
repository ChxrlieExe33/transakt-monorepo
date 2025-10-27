package com.cdcrane.customers.service;

public interface EmailUseCase {

    void sendVerificationEmail(String email, String firstName, int verificationCode);
}
