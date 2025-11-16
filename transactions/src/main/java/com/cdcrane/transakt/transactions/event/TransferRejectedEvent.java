package com.cdcrane.transakt.transactions.event;

import java.util.UUID;

public record TransferRejectedEvent(UUID transferId, String reason) {
}
