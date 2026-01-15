package com.mendel.transactions.usecase;

import com.mendel.transactions.domain.UpdateAttempt;
import com.mendel.transactions.repository.UpdateAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetUpdateAttemptsUseCase {

    private final UpdateAttemptRepository updateAttemptRepository;

    public List<UpdateAttempt> execute() {
        return updateAttemptRepository.findAll();
    }
}
