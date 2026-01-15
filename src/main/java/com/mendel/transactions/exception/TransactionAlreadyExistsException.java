package com.mendel.transactions.exception;

public class TransactionAlreadyExistsException extends RuntimeException {

    public TransactionAlreadyExistsException(Long id) {
        super("Transaction with id " + id + " already exists");
    }
}
