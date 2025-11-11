package com.cdcrane.transakt.transactions.controller;

import com.cdcrane.transakt.transactions.dto.CashDepositRequest;
import com.cdcrane.transakt.transactions.dto.CashWithdrawalRequest;
import com.cdcrane.transakt.transactions.service.CashOperationsUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cash")
public class CashOperationsController {

    private final CashOperationsUseCase cashOperationsUseCase;

    public CashOperationsController(CashOperationsUseCase cashOperationsUseCase) {
        this.cashOperationsUseCase = cashOperationsUseCase;
    }

    @PostMapping("/cash-deposit")
    public ResponseEntity<Void> cashDeposit(@RequestHeader(name = "Transakt-Customer-Id") UUID customerId, @RequestBody CashDepositRequest data) {

        cashOperationsUseCase.depositCash(data, customerId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PostMapping("/withdraw-cash")
    public ResponseEntity<Void> withdrawCash(@RequestHeader(name = "Transakt-Customer-Id") UUID customerId, @RequestBody CashWithdrawalRequest data) {

        cashOperationsUseCase.withdrawCash(data, customerId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
