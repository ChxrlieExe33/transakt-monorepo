package com.cdcrane.transakt.transactions.event;

import java.util.UUID;

public record TransferCompleteEvent(UUID transferId) {
}
