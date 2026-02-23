package com.xndfinance.service.user;

import com.xndfinance.model.User;
import com.xndfinance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {
        log.info("Creating new user with email: {}", user.getEmail());
        
        userRepository.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    throw new RuntimeException("User with email " + user.getEmail() + " already exists");
                });
        
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
        return savedUser;
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(UUID id) {
        log.debug("Fetching user with ID: {}", id);
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        log.debug("Fetching user with email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByName(String name) {
        log.debug("Fetching user with name: {}", name);
        return userRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        log.debug("Fetching all users");
        return userRepository.findAll();
    }

    public User updateUser(UUID id, User userDetails) {
        log.info("Updating user with ID: {}", id);
        
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(userDetails.getName());
                    existingUser.setEmail(userDetails.getEmail());
                    if (userDetails.getPassword() != null && !userDetails.getPassword().isBlank()) {
                        existingUser.setPassword(userDetails.getPassword());
                    }
                    User updatedUser = userRepository.save(existingUser);
                    log.info("User updated successfully with ID: {}", updatedUser.getId());
                    return updatedUser;
                })
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new RuntimeException("User not found with id: " + id);
                });
    }

    public void deleteUserById(UUID id) {
        log.info("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            log.error("User not found with ID: {}", id);
            throw new RuntimeException("User not found with id: " + id);
        }
        
        userRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
    }
}
