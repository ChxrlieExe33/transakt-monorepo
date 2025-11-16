package com.cdcrane.transakt.accounts.event;

import java.util.UUID;

public record TransferCompleteEvent(UUID transferId) {
}
