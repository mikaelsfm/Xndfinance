package com.xndfinance.service.transaction;

import com.xndfinance.dto.transaction.CreateTransactionDTO;
import com.xndfinance.exception.ApiException;
import com.xndfinance.model.Account;
import com.xndfinance.model.Transaction;
import com.xndfinance.repository.AccountRepository;
import com.xndfinance.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final AccountRepository accountRepository;

    @Transactional
    public Transaction createTransaction(CreateTransactionDTO transactionDTO) {
        log.info("Creating new transaction");

        UUID accountId = transactionDTO.accountId();
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("Account not found for id: {}", accountId);
                    return new ApiException("Account not found", HttpStatus.NOT_FOUND);
                });

        if (transactionDTO.amount() == null || transactionDTO.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException("Amount must be greater than zero", HttpStatus.BAD_REQUEST);
        }

        BigDecimal currentBalance = account.getBalance();
        BigDecimal amount = transactionDTO.amount();

        switch (transactionDTO.transactionType()) {
            case INCOME -> {
                currentBalance = currentBalance.add(amount);
            }
            case EXPENSE -> {
                if (currentBalance.compareTo(amount) < 0) {
                    throw new ApiException("Insufficient balance", HttpStatus.BAD_REQUEST);
                }
                currentBalance = currentBalance.subtract(amount);
            }
            default -> throw new ApiException("Invalid transaction type", HttpStatus.BAD_REQUEST);
        }

        Transaction updatedAccount = Transaction.builder()
                .accountId(transactionDTO.accountId())
                .amount(transactionDTO.amount())
                .transactionType(transactionDTO.transactionType())
                .category(transactionDTO.category())
                .description(transactionDTO.description())
                .transactionTime(LocalDateTime.now())
                .build();

        Transaction savedTransaction = transactionRepository.save(updatedAccount);

        log.info("Transaction created successfully. Transaction id: {}", savedTransaction.getId());

        return savedTransaction;
    }

    @Transactional
    public void deleteTransaction(UUID transactionId) {
        log.info("Deleting transaction: {}", transactionId);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> {
                    log.error("Transaction not found: {}", transactionId);
                    return new ApiException("Transaction not found", HttpStatus.NOT_FOUND);
                });

        Account account = accountRepository.findById(transaction.getAccountId())
                .orElseThrow(() -> {
                    log.error("Account not found for transaction: {}", transactionId);
                    return new ApiException("Account not found", HttpStatus.NOT_FOUND);
                });

        BigDecimal currentBalance = account.getBalance();
        BigDecimal amount = transaction.getAmount();

        if (currentBalance != null) {
            switch (transaction.getTransactionType()) {
                case INCOME -> {
                    if (amount != null && currentBalance.compareTo(amount) < 0) {
                        throw new ApiException("Inconsistent balance state", HttpStatus.BAD_REQUEST);
                    }
                    if (amount != null) {
                        currentBalance = currentBalance.subtract(amount);
                    }
                }
                case EXPENSE -> {
                    if (amount != null) {
                        currentBalance = currentBalance.add(amount);
                    }
                }
            }
            account.setBalance(currentBalance);
        }

        accountRepository.save(account);
        transactionRepository.delete(transaction);
        log.info("Transaction deleted and balance reverted successfully: {}", transactionId);
    }
}
