package com.cdcrane.transakt.transactions.entity;

import com.cdcrane.transakt.transactions.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.cdcrane.transakt.transactions.enums.TransactionProjectionStatus;

import java.time.Instant;
import java.util.UUID;

/**
 * This is used to list all transactions of any type in a timeline.
 * It is simply a projection, not the source of truth.
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "TransactionsSVC_transaction_projections")
public class TransactionProjection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;

    private UUID affectedAccountId;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionProjectionStatus transactionStatus;

    private Double amount;

    // Only one of the following 3 will be populated.
    private UUID transferId;

    private UUID cashDepositId;

    private UUID cashWithdrawalId;

    @CreatedDate
    private Instant processedAt;
}
