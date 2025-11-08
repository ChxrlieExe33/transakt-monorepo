package com.cdcrane.transakt.transactions.controller;

import com.cdcrane.transakt.transactions.dto.CashDepositRequest;
import com.cdcrane.transakt.transactions.service.CashDepositUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/deposit")
public class CashDepositController {

    private final CashDepositUseCase cashDepositUseCase;

    public CashDepositController(CashDepositUseCase cashDepositUseCase) {
        this.cashDepositUseCase = cashDepositUseCase;
    }

    @PostMapping("/cash-deposit")
    public ResponseEntity<Void> cashDeposit(@RequestHeader(name = "Transakt-Customer-Id") UUID customerId, @RequestBody CashDepositRequest data) {

        cashDepositUseCase.depositCash(data, customerId);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }
}
