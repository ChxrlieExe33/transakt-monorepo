package com.cdcrane.transakt.transactions.repository;

import com.cdcrane.transakt.transactions.entity.CashWithdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CashWithdrawalRepository extends JpaRepository<CashWithdrawal, UUID> {
}
