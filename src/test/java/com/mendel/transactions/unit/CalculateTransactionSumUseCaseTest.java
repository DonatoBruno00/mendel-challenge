package com.mendel.transactions.unit;

import com.mendel.transactions.domain.Transaction;
import com.mendel.transactions.domain.valueObject.Amount;
import com.mendel.transactions.domain.valueObject.TransactionId;
import com.mendel.transactions.domain.valueObject.TransactionType;
import com.mendel.transactions.repository.TransactionRepository;
import com.mendel.transactions.usecase.CalculateTransactionSumUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculateTransactionSumUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    private CalculateTransactionSumUseCase calculateTransactionSumUseCase;

    @BeforeEach
    void setUp() {
        calculateTransactionSumUseCase = new CalculateTransactionSumUseCase(transactionRepository);
    }

    @Test
    void shouldReturnTransactionAmountWhenNoChildren() {
        TransactionId id = TransactionId.of(10L);
        Transaction transaction = Transaction.builder()
                .id(id)
                .amount(Amount.of(5000))
                .type(TransactionType.of("cars"))
                .parentId(Optional.empty())
                .build();

        when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));
        when(transactionRepository.findAll()).thenReturn(List.of(transaction));

        double result = calculateTransactionSumUseCase.execute(id);

        assertEquals(5000, result);
    }

    @Test
    void shouldReturnSumIncludingChildren() {
        TransactionId parentId = TransactionId.of(10L);
        TransactionId childId = TransactionId.of(11L);

        Transaction parent = Transaction.builder()
                .id(parentId)
                .amount(Amount.of(5000))
                .type(TransactionType.of("cars"))
                .parentId(Optional.empty())
                .build();

        Transaction child = Transaction.builder()
                .id(childId)
                .amount(Amount.of(3000))
                .type(TransactionType.of("shopping"))
                .parentId(Optional.of(parentId))
                .build();

        when(transactionRepository.findById(parentId)).thenReturn(Optional.of(parent));
        when(transactionRepository.findAll()).thenReturn(List.of(parent, child));

        double result = calculateTransactionSumUseCase.execute(parentId);

        assertEquals(8000, result);
    }

    @Test
    void shouldReturnSumWithNestedChildren() {
        TransactionId grandparentId = TransactionId.of(10L);
        TransactionId parentId = TransactionId.of(11L);
        TransactionId childId = TransactionId.of(12L);

        Transaction grandparent = Transaction.builder()
                .id(grandparentId)
                .amount(Amount.of(5000))
                .type(TransactionType.of("cars"))
                .parentId(Optional.empty())
                .build();

        Transaction parent = Transaction.builder()
                .id(parentId)
                .amount(Amount.of(3000))
                .type(TransactionType.of("shopping"))
                .parentId(Optional.of(grandparentId))
                .build();

        Transaction child = Transaction.builder()
                .id(childId)
                .amount(Amount.of(1000))
                .type(TransactionType.of("electronics"))
                .parentId(Optional.of(parentId))
                .build();

        when(transactionRepository.findById(grandparentId)).thenReturn(Optional.of(grandparent));
        when(transactionRepository.findAll()).thenReturn(List.of(grandparent, parent, child));

        double result = calculateTransactionSumUseCase.execute(grandparentId);

        assertEquals(9000, result);
    }

    @Test
    void shouldReturnZeroWhenTransactionNotFound() {
        TransactionId id = TransactionId.of(999L);

        when(transactionRepository.findById(id)).thenReturn(Optional.empty());

        double result = calculateTransactionSumUseCase.execute(id);

        assertEquals(0.0, result);
    }
}
