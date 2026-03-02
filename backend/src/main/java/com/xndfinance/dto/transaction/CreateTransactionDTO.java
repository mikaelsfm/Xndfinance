package com.xndfinance.dto.transaction;

import com.xndfinance.enumerators.TransactionTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransactionDTO(

        @NotBlank(message = "AccountId is required")
        @Pattern(regexp = "^[0-9a-fA-F\\-]{36}$", message = "AccountId must be a valid UUID (36 characters)")
        UUID accountId,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        BigDecimal amount,

        @NotNull(message = "Transaction type is required")
        TransactionTypeEnum transactionType,

        @NotBlank(message = "Category is required")
        String category,

        String description
) {}