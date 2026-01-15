package com.mendel.transactions.usecase;

import com.mendel.transactions.domain.valueObject.TransactionType;
import com.mendel.transactions.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetTransactionsByTypeUseCase {

    private final TransactionRepository transactionRepository;

    public List<Long> execute(TransactionType type) {
        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getType().getValue().equals(type.getValue()))
                .map(transaction -> transaction.getId().getValue())
                .toList();
    }
}
