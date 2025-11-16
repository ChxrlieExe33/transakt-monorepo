package com.cdcrane.transakt.accounts.event;

import java.util.UUID;

public record TransferRejectedEvent(UUID transferId, String reason) {
}
