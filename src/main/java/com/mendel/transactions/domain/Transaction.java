package com.mendel.transactions.domain;

import java.util.Optional;

import com.mendel.transactions.domain.valueObject.Amount;
import com.mendel.transactions.domain.valueObject.TransactionId;
import com.mendel.transactions.domain.valueObject.TransactionType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class Transaction {

	private final TransactionId id;
	private final Amount amount;
	private final TransactionType type;
	private final Optional<TransactionId> parentId;
}
