package com.cdcrane.transakt.accounts.dto;

import jakarta.validation.constraints.NotEmpty;

public record OpenBankAccountRequest(@NotEmpty String accountName) {
}
