package com.cdcrane.transakt.transactions.event;

import java.util.UUID;

public record AccountOpenedEvent(UUID accountId, Double balance, UUID customerId) {
}
