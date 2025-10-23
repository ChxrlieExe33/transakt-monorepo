package com.cdcrane.customers.dto;

import java.time.LocalDate;

public record RegisterCustomerRequest(String firstName,
                                      String lastName,
                                      String email,
                                      LocalDate birthDate,
                                      String address,
                                      String city,
                                      String jobTitle) {
}
