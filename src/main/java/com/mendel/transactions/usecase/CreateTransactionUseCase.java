package com.mendel.transactions.usecase;

import com.mendel.transactions.domain.Transaction;
import com.mendel.transactions.domain.valueObject.TransactionId;
import com.mendel.transactions.exception.TransactionAlreadyExistsException;
import com.mendel.transactions.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateTransactionUseCase {

    private final TransactionRepository transactionRepository;

    public Transaction execute(Transaction transaction) {
        TransactionId id = transaction.getId();

        if (transactionRepository.existsById(id)) {
            throw new TransactionAlreadyExistsException(id.getValue());
        }

        transactionRepository.save(transaction);
        return transaction;
    }
}
