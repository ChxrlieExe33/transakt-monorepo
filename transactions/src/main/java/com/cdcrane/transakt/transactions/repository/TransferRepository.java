package com.cdcrane.transakt.transactions.repository;

import com.cdcrane.transakt.transactions.entity.BankTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<BankTransfer, UUID> {
}
