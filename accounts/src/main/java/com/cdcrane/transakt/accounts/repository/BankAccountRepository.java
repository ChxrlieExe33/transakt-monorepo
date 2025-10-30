package com.cdcrane.transakt.accounts.repository;

import com.cdcrane.transakt.accounts.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {

    Boolean existsByCustomerId(UUID customerId);
}
