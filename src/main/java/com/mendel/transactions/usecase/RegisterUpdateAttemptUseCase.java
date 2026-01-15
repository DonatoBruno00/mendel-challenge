package com.mendel.transactions.usecase;

import com.mendel.transactions.domain.UpdateAttempt;
import com.mendel.transactions.domain.valueObject.TransactionId;
import com.mendel.transactions.repository.UpdateAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterUpdateAttemptUseCase {

    private final UpdateAttemptRepository updateAttemptRepository;

    public void execute(TransactionId transactionId) {
        UpdateAttempt attempt = UpdateAttempt.of(transactionId);
        updateAttemptRepository.save(attempt);
    }
}
