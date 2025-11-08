package com.cdcrane.transakt.transactions.dto;

import java.util.UUID;

public record CashDepositRequest(UUID accountId, Double amount, String concept) {
}
