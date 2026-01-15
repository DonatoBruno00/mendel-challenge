package com.mendel.transactions.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateTransactionResponseDto {

    private final String status;

    public static CreateTransactionResponseDto ok() {
        return new CreateTransactionResponseDto("ok");
    }
}
