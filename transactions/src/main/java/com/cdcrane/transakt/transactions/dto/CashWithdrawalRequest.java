package com.cdcrane.transakt.transactions.dto;

import java.util.UUID;

public record CashWithdrawalRequest(UUID accountId, Double amount, String concept) {
}
