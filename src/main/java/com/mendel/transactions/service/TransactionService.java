package com.mendel.transactions.service;

import com.mendel.transactions.domain.Transaction;
import com.mendel.transactions.dto.CreateTransactionRequestDto;
import com.mendel.transactions.dto.CreateTransactionResponseDto;
import com.mendel.transactions.mapper.TransactionMapper;
import com.mendel.transactions.usecase.CreateTransactionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final TransactionMapper transactionMapper;

    public CreateTransactionResponseDto createTransaction(Long id, CreateTransactionRequestDto request) {
        Transaction transaction = transactionMapper.toTransaction(id, request);
        createTransactionUseCase.execute(transaction);
        return CreateTransactionResponseDto.ok();
    }
}
