package com.cdcrane.transakt.accounts.controller;

import com.cdcrane.transakt.accounts.dto.BankAccountOpenedResponse;
import com.cdcrane.transakt.accounts.dto.OpenBankAccountRequest;
import com.cdcrane.transakt.accounts.service.BankAccountUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class BankAccountController {

    private final BankAccountUseCase bankAccountUseCase;

    public BankAccountController(BankAccountUseCase bankAccountUseCase) {
        this.bankAccountUseCase = bankAccountUseCase;
    }

    @PostMapping
    public ResponseEntity<BankAccountOpenedResponse> openBankAccount(@RequestHeader(value = "Transakt-Customer-Id") UUID customerId, @RequestBody OpenBankAccountRequest data) {

        BankAccountOpenedResponse response = bankAccountUseCase.openBankAccount(customerId, data);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

}
