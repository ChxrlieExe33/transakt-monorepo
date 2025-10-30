package com.cdcrane.transakt.accounts.repository;

import com.cdcrane.transakt.accounts.entity.CustomerProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerProjectionRepository extends JpaRepository<CustomerProjection, UUID> {
}
