package com.cdcrane.transakt.accounts.event;

import java.util.UUID;

public record CustomerVerifiedEvent(UUID customerId,
                                    String firstName,
                                    String lastName,
                                    String email,
                                    String temporaryPassword) {
}
