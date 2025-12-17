package com.cdcrane.transakt.transactions.controller;

import com.cdcrane.transakt.transactions.dto.TransactionProjectionDTO;
import com.cdcrane.transakt.transactions.service.TransactionProjectionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movements")
@RequiredArgsConstructor
public class TransactionsController {

    private final TransactionProjectionUseCase transactionProjectionUseCase;

    /**
     * Gets the transactions-projections of a specific account, business logic checks for authorisation.
     * @param customerId The current customer-id as a header autopopulated by the gateway server.
     * @param accountId The account-id whose movements are being queried.
     * @param pageable The pagination information.
     * @return A Page of DTOs, where the transactionId points to the actual ID of the transaction, not the projection ID,
     * so the frontend can build a correct URL to the specific movement with the transaction type.
     */
    @GetMapping("{accountId}")
    public ResponseEntity<Page<TransactionProjectionDTO>> getTransactionsByAccount(@RequestHeader(name = "Transakt-Customer-Id") UUID customerId, @PathVariable UUID accountId,  Pageable pageable) {

        var data = transactionProjectionUseCase.getTransactionsByAffectedAccount(accountId, customerId, pageable);

        return ResponseEntity.ok(data);

    }

}
