package com.mendel.transactions.domain;

import com.mendel.transactions.domain.valueObject.TransactionId;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public final class UpdateAttempt {

    private final TransactionId transactionId;
    private final Instant attemptedAt;

    public static UpdateAttempt of(TransactionId transactionId) {
        return UpdateAttempt.builder()
                .transactionId(transactionId)
                .attemptedAt(Instant.now())
                .build();
    }
}
