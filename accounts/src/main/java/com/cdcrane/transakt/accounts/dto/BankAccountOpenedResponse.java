package com.cdcrane.transakt.accounts.dto;

import java.util.UUID;

public record BankAccountOpenedResponse(UUID accountId, Double balance) {
}
