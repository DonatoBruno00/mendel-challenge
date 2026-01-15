package com.mendel.transactions.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetTransactionsByTypeResponseDto {

    private final List<Long> transactions;
}
