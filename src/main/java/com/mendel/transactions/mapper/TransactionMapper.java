package com.mendel.transactions.mapper;

import com.mendel.transactions.domain.Transaction;
import com.mendel.transactions.domain.valueObject.Amount;
import com.mendel.transactions.domain.valueObject.TransactionId;
import com.mendel.transactions.domain.valueObject.TransactionType;
import com.mendel.transactions.dto.CreateTransactionRequestDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransactionMapper {

    public Transaction toTransaction(Long id, CreateTransactionRequestDto request) {
        Optional<TransactionId> parentId = Optional.ofNullable(request.getParentId())
                .map(value -> TransactionId.of(value));

        return Transaction.builder()
                .id(TransactionId.of(id))
                .amount(Amount.of(request.getAmount()))
                .type(TransactionType.of(request.getType()))
                .parentId(parentId)
                .build();
    }
}
