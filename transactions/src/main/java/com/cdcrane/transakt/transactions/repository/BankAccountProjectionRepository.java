package com.cdcrane.transakt.transactions.repository;

import com.cdcrane.transakt.transactions.entity.BankAccountProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BankAccountProjectionRepository extends JpaRepository<BankAccountProjection, UUID> {

    Boolean existsByAccountId(UUID accountId);
}
