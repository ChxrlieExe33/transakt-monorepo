package com.cdcrane.transakt.accounts.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "AccountsSVC_accounts")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountId;

    private UUID customerId;

    private String accountName;

    private Double currentBalance;

    private Boolean enabled;

    private Boolean overdraftPermitted;

    @CreatedDate
    private Instant openedAt;
}
