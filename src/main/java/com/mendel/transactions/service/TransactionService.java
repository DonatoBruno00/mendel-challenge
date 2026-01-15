package com.mendel.transactions.service;

import com.mendel.transactions.domain.Transaction;
import com.mendel.transactions.domain.valueObject.TransactionId;
import com.mendel.transactions.domain.valueObject.TransactionType;
import com.mendel.transactions.dto.CreateTransactionRequestDto;
import com.mendel.transactions.dto.CreateTransactionResponseDto;
import com.mendel.transactions.dto.GetTransactionSumResponseDto;
import com.mendel.transactions.dto.GetTransactionsByTypeResponseDto;
import com.mendel.transactions.mapper.TransactionMapper;
import com.mendel.transactions.usecase.CalculateTransactionSumUseCase;
import com.mendel.transactions.usecase.CreateTransactionUseCase;
import com.mendel.transactions.usecase.GetTransactionsByTypeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final GetTransactionsByTypeUseCase getTransactionsByTypeUseCase;
    private final CalculateTransactionSumUseCase calculateTransactionSumUseCase;
    private final TransactionMapper transactionMapper;

    public CreateTransactionResponseDto createTransaction(Long id, CreateTransactionRequestDto request) {
        Transaction transaction = transactionMapper.toTransaction(id, request);
        createTransactionUseCase.execute(transaction);
        return CreateTransactionResponseDto.ok();
    }

    public GetTransactionsByTypeResponseDto getTransactionsByType(String type) {
        List<Long> transactionIds = getTransactionsByTypeUseCase.execute(TransactionType.of(type));
        return transactionMapper.toGetTransactionsByTypeResponse(transactionIds);
    }

    public GetTransactionSumResponseDto getTransactionSum(Long id) {
        double sum = calculateTransactionSumUseCase.execute(TransactionId.of(id));
        return transactionMapper.toGetTransactionSumResponse(sum);
    }
}
