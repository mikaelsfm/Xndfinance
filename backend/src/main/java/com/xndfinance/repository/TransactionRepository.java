package com.xndfinance.repository;

import com.xndfinance.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findById(UUID id);

    List<Transaction> findByAccountId(UUID accountId);
}
