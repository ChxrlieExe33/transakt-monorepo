package com.cdcrane.customers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SubmitVerificationCodeRequest(@NotNull Integer verificationCode,
                                            @NotNull @Email String email) {
}
