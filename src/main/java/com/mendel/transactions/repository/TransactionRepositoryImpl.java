package com.mendel.transactions.repository;

import com.mendel.transactions.domain.Transaction;
import com.mendel.transactions.domain.valueObject.TransactionId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final Map<Long, Transaction> ledger = new ConcurrentHashMap<>();

    @Override
    public void save(Transaction transaction) {
        ledger.put(transaction.getId().getValue(), transaction);
    }

    @Override
    public boolean existsById(TransactionId id) {
        return ledger.containsKey(id.getValue());
    }

    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(ledger.values());
    }

    @Override
    public Optional<Transaction> findById(TransactionId id) {
        return Optional.ofNullable(ledger.get(id.getValue()));
    }
}
