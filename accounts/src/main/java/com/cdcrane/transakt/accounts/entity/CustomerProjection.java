package com.cdcrane.transakt.accounts.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "AccountsSVC_customer_projection")
public class CustomerProjection {

    @Id
    private UUID customerId;

    @Column(name = "new_account_creation_permitted")
    private Boolean newAccountCreationPermitted;

    Boolean customerVerified;

}
