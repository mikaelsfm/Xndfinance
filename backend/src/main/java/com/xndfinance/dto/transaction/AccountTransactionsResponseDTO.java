package com.xndfinance.dto.transaction;

import com.xndfinance.enumerators.AccountTypeEnum;
import com.xndfinance.model.Transaction;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class AccountTransactionsResponseDTO {

    private UUID accountId;
    private AccountTypeEnum type;
    private List<Transaction> transactions;

}
