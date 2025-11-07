package com.cdcrane.customers.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "CustomersSVC_customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID customerId;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDate birthDate;

    private String address;

    private String city;

    private String jobTitle;

    private Integer verificationCode;

    private Boolean verified;

    @CreatedDate
    private Instant registeredAt;

}
