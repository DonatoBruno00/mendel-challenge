package com.mendel.transactions.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateAttemptResponseDto {

    private final Long transactionId;
    private final String attemptedAt;
}
