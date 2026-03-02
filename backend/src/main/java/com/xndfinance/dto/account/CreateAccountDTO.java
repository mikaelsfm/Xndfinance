package com.xndfinance.dto.account;

import com.xndfinance.enumerators.AccountTypeEnum;
import jakarta.validation.constraints.NotNull;

public record CreateAccountDTO(
        @NotNull
        AccountTypeEnum type,

        @NotNull
        String userId
) {
}
