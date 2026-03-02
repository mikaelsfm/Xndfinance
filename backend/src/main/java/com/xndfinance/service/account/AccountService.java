package com.xndfinance.service.account;

import com.xndfinance.dto.account.CreateAccountDTO;
import com.xndfinance.exception.ApiException;
import com.xndfinance.model.Account;
import com.xndfinance.repository.AccountRepository;
import com.xndfinance.service.user.UserQueryService;
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

    private final AccountRepository accountRepository;
    private final UserQueryService userQueryService;
    private final AccountQueryService accountQueryService;

    public Account createAccount(CreateAccountDTO accountRequestDTO) {
        log.info("Creating account for user: {}", accountRequestDTO.userId());

        boolean alreadyExists = accountQueryService.findByUserId(accountRequestDTO.userId())
                .stream()
                .anyMatch(account -> account.getType() == accountRequestDTO.type());

        if (alreadyExists) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Account of type " + accountRequestDTO.type() +
                            " for user " + accountRequestDTO.userId() + " already exists"
            );
        }

        Account account = Account.builder()
                .type(accountRequestDTO.type())
                .balance(new BigDecimal(0))
                .userId(accountRequestDTO.userId())
                .createdAt(LocalDateTime.now())
                .build();

        Account saved = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", saved.getId());
        return saved;
    }

    public Account updateBalance(UUID accountId, BigDecimal newBalance) {
        log.info("Updating balance for account: {}", accountId);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new ApiException("Balance cannot be negative", HttpStatus.BAD_REQUEST);
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("Account not found: {}", accountId);
                    return new ApiException(HttpStatus.NOT_FOUND, "Account not found");
                });

        account.setBalance(newBalance);
        Account updated = accountRepository.save(account);
        log.info("Balance updated successfully for account: {}", updated.getId());
        return updated;
    }

    public void deleteAccount(UUID accountId) {
        log.info("Deleting account: {}", accountId);

        if (!accountRepository.existsById(accountId)) {
            log.error("Account not found: {}", accountId);
            throw new ApiException(HttpStatus.NOT_FOUND, "Account not found");
        }

        accountRepository.deleteById(accountId);
        log.info("Account deleted successfully: {}", accountId);

    }

}