package com.xndfinance.service.transaction;

import com.xndfinance.model.Account;
import com.xndfinance.model.Transaction;
import com.xndfinance.repository.AccountRepository;
import com.xndfinance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionQueryService {

    private final TransactionRepository transactionRepository;

    private final AccountRepository accountRepository;

    public Optional<Transaction> findById(UUID id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> findByAccountId(UUID accountId) {
        return transactionRepository.findByAccountId(accountId); }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
}
