package com.cdcrane.transakt.transactions.event;

import java.util.UUID;

public record CashWithdrawnEvent(UUID cashWithdrawalId, UUID accountId, Double amount, String concept) {
}
