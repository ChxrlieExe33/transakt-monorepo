package com.cdcrane.transakt.accounts.event;

import java.util.UUID;

public record AccountOpenedEvent(UUID accountId, Double balance, UUID customerId) {
}
