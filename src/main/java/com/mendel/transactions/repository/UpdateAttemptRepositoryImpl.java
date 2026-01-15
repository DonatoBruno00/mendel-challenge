package com.mendel.transactions.repository;

import com.mendel.transactions.domain.UpdateAttempt;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class UpdateAttemptRepositoryImpl implements UpdateAttemptRepository {

    private final List<UpdateAttempt> attempts = new CopyOnWriteArrayList<>();

    @Override
    public void save(UpdateAttempt updateAttempt) {
        attempts.add(updateAttempt);
    }

    @Override
    public List<UpdateAttempt> findAll() {
        return new ArrayList<>(attempts);
    }
}
