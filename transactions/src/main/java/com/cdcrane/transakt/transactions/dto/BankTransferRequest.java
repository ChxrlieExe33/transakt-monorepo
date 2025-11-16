package com.cdcrane.transakt.transactions.dto;

import java.util.UUID;

public record BankTransferRequest(UUID sourceAccountId, UUID destinationAccountId, Double amount, String sourceName, String destinationName, String concept) {
}
