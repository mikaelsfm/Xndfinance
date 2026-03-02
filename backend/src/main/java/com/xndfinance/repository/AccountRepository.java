package com.xndfinance.repository;

import com.xndfinance.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    List<Account> findByUserId(String userId);

    List<Account> findAccountListByUserId(String userId);
}
