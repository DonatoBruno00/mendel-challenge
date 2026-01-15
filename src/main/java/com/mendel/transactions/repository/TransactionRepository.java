package com.mendel.transactions.repository;

import com.mendel.transactions.domain.Transaction;
import com.mendel.transactions.domain.valueObject.TransactionId;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    void save(Transaction transaction);

    boolean existsById(TransactionId id);

    List<Transaction> findAll();

    Optional<Transaction> findById(TransactionId id);
}
