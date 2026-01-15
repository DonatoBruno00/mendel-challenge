package com.mendel.transactions.repository;

import com.mendel.transactions.domain.UpdateAttempt;

import java.util.List;

public interface UpdateAttemptRepository {

    void save(UpdateAttempt updateAttempt);

    List<UpdateAttempt> findAll();
}
