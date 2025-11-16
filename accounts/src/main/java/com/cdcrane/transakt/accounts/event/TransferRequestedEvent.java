package com.cdcrane.transakt.accounts.event;

import java.util.UUID;

public record TransferRequestedEvent(UUID transferId, UUID sourceAccountId, UUID destinationAccountId, Double amount, String sourceName, String destinationName, String concept) {
}

