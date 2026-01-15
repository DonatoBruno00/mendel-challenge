package com.mendel.transactions.repository;

import com.mendel.transactions.domain.Transaction;
import com.mendel.transactions.domain.valueObject.TransactionId;

public interface TransactionRepository {

    void save(Transaction transaction);

    boolean existsById(TransactionId id);
}
