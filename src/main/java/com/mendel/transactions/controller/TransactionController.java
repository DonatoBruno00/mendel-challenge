package com.mendel.transactions.controller;

import com.mendel.transactions.dto.CreateTransactionRequestDto;
import com.mendel.transactions.dto.CreateTransactionResponseDto;
import com.mendel.transactions.dto.GetTransactionSumResponseDto;
import com.mendel.transactions.dto.GetTransactionsByTypeResponseDto;
import com.mendel.transactions.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PutMapping("/{transactionId}")
    public ResponseEntity<CreateTransactionResponseDto> createTransaction(
            @PathVariable Long transactionId,
            @Valid @RequestBody CreateTransactionRequestDto request
    ) {
        CreateTransactionResponseDto response = transactionService.createTransaction(transactionId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/types/{type}")
    public ResponseEntity<GetTransactionsByTypeResponseDto> getTransactionsByType(@PathVariable String type) {
        GetTransactionsByTypeResponseDto response = transactionService.getTransactionsByType(type);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sum/{transactionId}")
    public ResponseEntity<GetTransactionSumResponseDto> getTransactionSum(@PathVariable Long transactionId) {
        GetTransactionSumResponseDto response = transactionService.getTransactionSum(transactionId);
        return ResponseEntity.ok(response);
    }
}
