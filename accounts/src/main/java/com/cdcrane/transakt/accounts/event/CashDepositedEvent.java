package com.cdcrane.transakt.accounts.event;

import java.util.UUID;

public record CashDepositedEvent(UUID cashDepositId, UUID accountId, Double amount, String concept) {
}
