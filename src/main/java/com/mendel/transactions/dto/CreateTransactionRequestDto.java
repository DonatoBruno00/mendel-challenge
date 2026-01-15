package com.mendel.transactions.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTransactionRequestDto {

    @NotNull(message = "amount is required")
    @PositiveOrZero(message = "amount must be zero or positive")
    @Schema(example = "5000")
    private Double amount;

    @NotBlank(message = "type is required")
    @Schema(example = "cars")
    private String type;

    @Schema(example = "10", nullable = true)
    private Long parentId;
}
