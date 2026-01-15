package com.mendel.transactions.mapper;

import com.mendel.transactions.domain.Transaction;
import com.mendel.transactions.domain.UpdateAttempt;
import com.mendel.transactions.domain.valueObject.Amount;
import com.mendel.transactions.domain.valueObject.TransactionId;
import com.mendel.transactions.domain.valueObject.TransactionType;
import com.mendel.transactions.dto.CreateTransactionRequestDto;
import com.mendel.transactions.dto.GetTransactionSumResponseDto;
import com.mendel.transactions.dto.GetTransactionsByTypeResponseDto;
import com.mendel.transactions.dto.UpdateAttemptResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TransactionMapper {

    public Transaction toTransaction(Long id, CreateTransactionRequestDto request) {
        Optional<TransactionId> parentId = Optional.ofNullable(request.getParentId())
                .map(parentMapped -> TransactionId.of(parentMapped));

        return Transaction.builder()
                .id(TransactionId.of(id))
                .amount(Amount.of(request.getAmount()))
                .type(TransactionType.of(request.getType()))
                .parentId(parentId)
                .build();
    }

    public GetTransactionsByTypeResponseDto toGetTransactionsByTypeResponse(List<Long> transactionIds) {
        return new GetTransactionsByTypeResponseDto(transactionIds);
    }

    public GetTransactionSumResponseDto toGetTransactionSumResponse(double sum) {
        return new GetTransactionSumResponseDto(sum);
    }

    public UpdateAttemptResponseDto toUpdateAttemptResponseDto(UpdateAttempt attempt) {
        return new UpdateAttemptResponseDto(
                attempt.getTransactionId().getValue(),
                attempt.getAttemptedAt().toString()
        );
    }

    public List<UpdateAttemptResponseDto> toUpdateAttemptResponseDtoList(List<UpdateAttempt> attempts) {
        return attempts.stream()
                .map(this::toUpdateAttemptResponseDto)
                .toList();
    }
}
