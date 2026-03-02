package com.xndfinance.controller;

import com.xndfinance.dto.account.CreateAccountDTO;
import com.xndfinance.dto.account.UpdateBalanceDTO;
import com.xndfinance.model.Account;
import com.xndfinance.service.account.AccountQueryService;
import com.xndfinance.service.account.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Slf4j
public class AccountController {
    private final AccountService accountService;
    private final AccountQueryService accountQueryService;

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountDTO accountRequestDTO) {
        log.info("Creating account with type: {}", accountRequestDTO.type());
        Account createdAccount = accountService.createAccount(accountRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        log.debug("Fetching all accounts");
        List<Account> accounts = accountQueryService.findAll();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable UUID id) {
        log.debug("Fetching account with ID: {}", id);
        return accountQueryService.findById(id)
                .map(account -> ResponseEntity.ok(account))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUserId(@PathVariable String userId) {
        log.debug("Fetching account for user: {}", userId);
        List<Account> accounts = accountQueryService.findAccountListByUserId(userId);
        return ResponseEntity.ok(accounts);
    }

    @PatchMapping("/{id}/balance")
    public ResponseEntity<Account> updateBalance(@PathVariable UUID id, @Valid @RequestBody UpdateBalanceDTO balanceDTO) {
        Account updated = accountService.updateBalance(id, balanceDTO.balance());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
        log.info("Deleting account with ID: {}", id);
        accountService.deleteAccount(id);
        return ResponseEntity.ok().build();
    }
}
