package com.cdcrane.transakt.transactions.repository;

import com.cdcrane.transakt.transactions.entity.CashDeposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CashDepositRepository extends JpaRepository<CashDeposit, UUID> {
}
