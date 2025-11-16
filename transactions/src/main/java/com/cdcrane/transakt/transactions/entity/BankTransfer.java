package com.cdcrane.transakt.transactions.entity;

import com.cdcrane.transakt.transactions.enums.BankTransferStatus;
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
@Table(name = "TransactionsSVC_bank_transfers")
public class BankTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bankTransferId;

    private UUID sourceAccountId;

    private UUID targetAccountId;

    private String sourceName;

    private String targetName;

    private Double amount;

    private String concept;

    @Enumerated(EnumType.STRING)
    private BankTransferStatus status;

    private String failureReason;

    @CreatedDate
    private Instant processedAt;
}
