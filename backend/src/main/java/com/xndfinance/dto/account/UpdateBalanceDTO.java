package com.xndfinance.dto.account;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateBalanceDTO(
        @NotNull(message = "Balance is required")
        @DecimalMin(value = "0.00", inclusive = true, message = "Balance cannot be negative")
        BigDecimal balance
) {}
