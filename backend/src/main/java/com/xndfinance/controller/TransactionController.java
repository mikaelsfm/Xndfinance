package com.xndfinance.controller;

import com.xndfinance.model.Transaction;
import com.xndfinance.service.transaction.TransactionQueryService;
import com.xndfinance.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionQueryService transactionQueryService;

    @PostMapping("/create")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        log.info("Creating transaction", transaction.getId());
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return  ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        log.debug("Retrieving all transactions");
        List<Transaction> transactions = transactionQueryService.findAll();
        return  ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable UUID id) {
        log.debug("Retrieving transaction with ID: {}", id);
        return transactionQueryService.findById(id)
                .map(transaction -> ResponseEntity.ok(transaction))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(@PathVariable UUID accountId) {
        log.debug("Retrieving all transactions for account", accountId);
        List<Transaction> transactions = transactionQueryService.findByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id) {
        log.info("Deleting transaction with ID: {}", id);
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }
}
