package com.cdcrane.customers.event;

import java.time.LocalDate;
import java.util.UUID;

public record CustomerRegisteredEvent(UUID customerId,
                                      String firstName,
                                      String lastName,
                                      String email,
                                      LocalDate birthDate,
                                      String address,
                                      String city,
                                      String jobTitle,
                                      Integer verificationCode) {
}
