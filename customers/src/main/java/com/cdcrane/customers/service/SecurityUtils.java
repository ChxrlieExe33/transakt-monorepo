package com.cdcrane.customers.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class SecurityUtils {

    private static final char[] ALPHANUM =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    public int generateVerificationCode() {

        SecureRandom random = new SecureRandom();

        return random.nextInt(900000) + 100000;

    }

    public String generateTemporaryPassword() {

        SecureRandom random = new SecureRandom();

        final int length = 8;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = random.nextInt(ALPHANUM.length);
            sb.append(ALPHANUM[idx]);
        }
        return sb.toString();

    }
}
