package com.mendel.transactions.unit;

import com.mendel.transactions.domain.Transaction;
import com.mendel.transactions.domain.valueObject.Amount;
import com.mendel.transactions.domain.valueObject.TransactionId;
import com.mendel.transactions.domain.valueObject.TransactionType;
import com.mendel.transactions.repository.TransactionRepository;
import com.mendel.transactions.usecase.GetTransactionsByTypeUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTransactionsByTypeUseCaseTest {

    private static final String UNKNOWN_TYPE = "unknown";

    @Mock
    private TransactionRepository transactionRepository;

    private GetTransactionsByTypeUseCase getTransactionsByTypeUseCase;

    @BeforeEach
    void setUp() {
        getTransactionsByTypeUseCase = new GetTransactionsByTypeUseCase(transactionRepository);
    }

    @Test
    void shouldReturnTransactionIdsForGivenType() {
        Transaction carTransaction1 = Transaction.builder()
                .id(TransactionId.of(10L))
                .amount(Amount.of(5000))
                .type(TransactionType.of("cars"))
                .parentId(Optional.empty())
                .build();

        Transaction carTransaction2 = Transaction.builder()
                .id(TransactionId.of(20L))
                .amount(Amount.of(3000))
                .type(TransactionType.of("cars"))
                .parentId(Optional.empty())
                .build();

        Transaction shoppingTransaction = Transaction.builder()
                .id(TransactionId.of(30L))
                .amount(Amount.of(1000))
                .type(TransactionType.of("shopping"))
                .parentId(Optional.empty())
                .build();

        when(transactionRepository.findAll()).thenReturn(List.of(carTransaction1, carTransaction2, shoppingTransaction));

        List<Long> result = getTransactionsByTypeUseCase.execute(TransactionType.of("cars"));

        assertEquals(List.of(10L, 20L), result);
        verify(transactionRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoTransactionsOfType() {
        when(transactionRepository.findAll()).thenReturn(List.of());

        List<Long> result = getTransactionsByTypeUseCase.execute(TransactionType.of(UNKNOWN_TYPE));

        assertTrue(result.isEmpty());
        verify(transactionRepository).findAll();
    }
}
