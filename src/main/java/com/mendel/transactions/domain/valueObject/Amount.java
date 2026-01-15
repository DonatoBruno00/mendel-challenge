package com.mendel.transactions.domain.valueObject;

import com.mendel.transactions.exception.DomainValidationException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Amount {

	private final double value;

	public static Amount of(double value) {
		if (value < 0) {
			throw new DomainValidationException("amount must be zero or positive");
		}
		return new Amount(value);
	}

}
