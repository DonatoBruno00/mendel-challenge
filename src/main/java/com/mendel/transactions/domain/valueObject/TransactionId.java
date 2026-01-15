package com.mendel.transactions.domain.valueObject;

import com.mendel.transactions.exception.DomainValidationException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class TransactionId {

	private final long value;

	public static TransactionId of(long value) {
		if (value <= 0) {
			throw new DomainValidationException("transaction_id must be positive");
		}
		return new TransactionId(value);
	}

}
