package com.cdcrane.customers.dto;

import java.time.Instant;
import java.util.UUID;

public record CustomerDTO(UUID customerId, String firstName, String lastName, String email, Instant registeredAt) {
}
