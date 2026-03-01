package com.xndfinance.service.account;

import com.xndfinance.exception.ApiException;
import com.xndfinance.model.Account;
import com.xndfinance.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountService {

    private final AccountRepository repository;

    public Account createAccount(Account account) {
        log.info("Creating account for user: {}", account.getUserId());

        if (repository.findByUserId(account.getUserId())
                .filter(existingAccount -> account.getType() == existingAccount.getType())
                .isPresent()) {
            throw new ApiException("Account of type " + account.getType() + "for user " + account.getUserId() + " already exists");
        }

        account.setCreatedAt(LocalDateTime.now());
        Account saved = repository.save(account);
        log.info("Account created successfully with ID: {}", saved.getId());
        return saved;

    }

    public Account updateBalance(UUID accountId, BigDecimal newBalance) {
        log.info("Updating balance for account: {}", accountId);

        Account account = repository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("Account not found: {}", accountId);
                    return new ApiException(HttpStatus.NOT_FOUND, "Account not found");
                });

        account.setBalance(newBalance);
        Account updated = repository.save(account);
        log.info("Balance updated successfully for account: {}", updated.getId());
        return updated;

    }

    public void deleteAccount(UUID accountId) {
        log.info("Deleting account: {}", accountId);

        if (!repository.existsById(accountId)) {
            log.error("Account not found: {}", accountId);
            throw new ApiException(HttpStatus.NOT_FOUND, "Account not found");
        }

        repository.deleteById(accountId);
        log.info("Account deleted successfully: {}", accountId);

    }

}