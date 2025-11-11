package com.cdcrane.transakt.accounts.event;

import java.util.UUID;

public record CashWithdrawnEvent(UUID cashWithdrawalId, UUID accountId, Double amount, String concept) {
}
