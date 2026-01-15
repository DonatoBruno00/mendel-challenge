package com.mendel.transactions.domain.valueObject;

import com.mendel.transactions.exception.DomainValidationException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class TransactionType {

	private final String value;

	public static TransactionType of(String value) {
		if (value == null || value.trim().isEmpty()) {
			throw new DomainValidationException("type must be provided");
		}
		return new TransactionType(value.trim());
	}

}
