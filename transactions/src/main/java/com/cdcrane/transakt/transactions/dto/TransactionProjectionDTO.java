package com.cdcrane.transakt.transactions.dto;

import com.cdcrane.transakt.transactions.enums.TransactionProjectionStatus;
import com.cdcrane.transakt.transactions.enums.TransactionType;

import java.util.UUID;

public record TransactionProjectionDTO(Double amount, TransactionType type, TransactionProjectionStatus status, UUID transactionId) {
}
