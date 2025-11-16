package com.cdcrane.transakt.transactions.controller;

import com.cdcrane.transakt.transactions.dto.BankTransferRequest;
import com.cdcrane.transakt.transactions.service.BankTransferUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bank-transfer")
public class BankTransferController {

    private final BankTransferUseCase bankTransferUseCase;

    public BankTransferController(BankTransferUseCase bankTransferUseCase) {
        this.bankTransferUseCase = bankTransferUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> transferMoney(@RequestHeader(name = "Transakt-Customer-Id") UUID currentCustomerId, @RequestBody BankTransferRequest request) {

        bankTransferUseCase.transferMoney(request, currentCustomerId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
