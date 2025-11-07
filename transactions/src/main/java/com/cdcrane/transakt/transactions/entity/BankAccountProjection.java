package com.cdcrane.transakt.transactions.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "TransactionsSVC_bank_accounts_projections")
public class BankAccountProjection {

    @Id
    private UUID accountId;

    private Double currentBalance;

    private UUID customerId;
}
