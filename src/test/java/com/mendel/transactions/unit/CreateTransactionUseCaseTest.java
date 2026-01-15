package com.mendel.transactions.unit;

import com.mendel.transactions.domain.Transaction;
import com.mendel.transactions.domain.valueObject.Amount;
import com.mendel.transactions.domain.valueObject.TransactionId;
import com.mendel.transactions.domain.valueObject.TransactionType;
import com.mendel.transactions.exception.TransactionAlreadyExistsException;
import com.mendel.transactions.repository.TransactionRepository;
import com.mendel.transactions.usecase.CreateTransactionUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTransactionUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    private CreateTransactionUseCase createTransactionUseCase;

    @BeforeEach
    void setUp() {
        createTransactionUseCase = new CreateTransactionUseCase(transactionRepository);
    }

    @Test
    void shouldCreateTransactionWhenIdDoesNotExist() {
        Transaction transaction = Transaction.builder()
                .id(TransactionId.of(1L))
                .amount(Amount.of(100.0))
                .type(TransactionType.of("cars"))
                .parentId(Optional.empty())
                .build();

        when(transactionRepository.existsById(transaction.getId())).thenReturn(false);

        Transaction result = createTransactionUseCase.execute(transaction);

        assertNotNull(result);
        assertEquals(transaction.getId(), result.getId());
        verify(transactionRepository).save(transaction);
    }

    @Test
    void shouldThrowExceptionWhenTransactionAlreadyExists() {
        Transaction transaction = Transaction.builder()
                .id(TransactionId.of(1L))
                .amount(Amount.of(100.0))
                .type(TransactionType.of("cars"))
                .parentId(Optional.empty())
                .build();

        when(transactionRepository.existsById(transaction.getId())).thenReturn(true);

        assertThrows(TransactionAlreadyExistsException.class, () -> {
            createTransactionUseCase.execute(transaction);
        });

        verify(transactionRepository, never()).save(any());
    }
}
