package com.cdcrane.customers.event;

public record CustomerVerifiedEvent(String firstName,
                                    String lastName,
                                    String email,
                                    String temporaryPassword) {
}
