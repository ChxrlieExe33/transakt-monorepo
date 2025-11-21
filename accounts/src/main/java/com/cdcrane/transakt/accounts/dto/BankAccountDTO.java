package com.cdcrane.transakt.accounts.dto;

import java.util.UUID;

public record BankAccountDTO(UUID accountId, String accountName, Double balance) {
}
