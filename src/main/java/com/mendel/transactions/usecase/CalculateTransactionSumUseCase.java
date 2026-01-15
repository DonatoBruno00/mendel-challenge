package com.mendel.transactions.usecase;

import com.mendel.transactions.domain.Transaction;
import com.mendel.transactions.domain.valueObject.TransactionId;
import com.mendel.transactions.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CalculateTransactionSumUseCase {

    private final TransactionRepository transactionRepository;

    public double execute(TransactionId transactionId) {
        return transactionRepository.findById(transactionId)
                .map(this::calculateSum)
                .orElse(0.0);
    }

    private double calculateSum(Transaction transaction) {
        double transactionAmount = transaction.getAmount().getValue();

        double linkedTransactionsAmount = findTransactionsLinkedTo(transaction.getId()).stream()
                .mapToDouble(this::calculateSum)
                .sum();

        return transactionAmount + linkedTransactionsAmount;
    }

    private List<Transaction> findTransactionsLinkedTo(TransactionId transactionId) {
        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getParentId().isPresent() &&
                        transaction.getParentId().get().getValue() == transactionId.getValue())
                .toList();
    }
}
