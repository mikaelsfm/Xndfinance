package com.xndfinance.service.user;

import com.xndfinance.model.User;
import com.xndfinance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository repository;

    public Optional<User> findById(UUID id) {
        return repository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}
