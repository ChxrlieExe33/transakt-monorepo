package com.cdcrane.transakt.transactions.repository;

import com.cdcrane.transakt.transactions.entity.TransactionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionProjectionRepository extends JpaRepository<TransactionProjection, UUID> {

    Page<TransactionProjection> findByAffectedAccountId(UUID accountId, Pageable pageable);

    Optional<TransactionProjection> findByTransferId(UUID transferId);
}
