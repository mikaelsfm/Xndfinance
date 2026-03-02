package com.xndfinance.service.account;

import com.xndfinance.model.Account;
import com.xndfinance.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountQueryService {

    private final AccountRepository repository;

    public Optional<Account> findById(UUID id) {
        return repository.findById(id);
    }

    public List<Account> findAll() {
        return repository.findAll();
    }

    public List<Account> findByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    public List<Account> findAccountListByUserId(String userId) {
        return repository.findAccountListByUserId(userId);
    }

}