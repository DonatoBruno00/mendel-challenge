package com.mendel.transactions.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetTransactionSumResponseDto {

    private final double sum;
}
