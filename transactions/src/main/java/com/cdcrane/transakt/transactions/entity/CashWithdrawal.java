package com.cdcrane.transakt.transactions.entity;

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
@Table(name = "TransactionsSVC_cash_withdrawals")
public class CashWithdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cashWithdrawalId;

    private Double amount;

    private String concept;

    private UUID accountId;

    @CreatedDate
    private Instant processedAt;
}
