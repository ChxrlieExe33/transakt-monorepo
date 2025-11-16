package com.cdcrane.transakt.transactions.event;

import java.util.UUID;

public record TransferRequestedEvent(UUID transferId, UUID sourceAccountId, UUID destinationAccountId, Double amount, String sourceName, String destinationName, String concept) {
}
