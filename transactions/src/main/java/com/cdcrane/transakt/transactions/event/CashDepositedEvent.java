package com.cdcrane.transakt.transactions.event;

import java.util.UUID;

public record CashDepositedEvent(UUID cashDepositId, UUID accountId, Double amount, String concept) {
}
