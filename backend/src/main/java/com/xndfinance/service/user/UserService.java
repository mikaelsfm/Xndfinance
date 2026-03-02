package com.xndfinance.service.user;

import com.xndfinance.dto.user.CreateUserRequestDTO;
import com.xndfinance.dto.user.UpdateUserDTO;
import com.xndfinance.dto.user.UserResponseDTO;
import com.xndfinance.exception.ApiException;
import com.xndfinance.model.User;
import com.xndfinance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public User createUser(CreateUserRequestDTO userDTO) {
        log.info("Creating new user with email: {}", userDTO.email());

        userRepository.findByEmail(userDTO.email())
                .ifPresent(existingUser -> {
                    throw new ApiException("User with email " + userDTO.email() + " already exists");
                });

        User user = User.builder()
                .name(userDTO.name())
                .email(userDTO.email())
                .password(userDTO.password())
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());

        return savedUser;
    }

    public UserResponseDTO updateUser(UUID id, UpdateUserDTO updateUserDTO) {
        log.info("Updating user with ID: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new ApiException("User not found with id: " + id);
                });

        if (updateUserDTO.name() != null && !updateUserDTO.name().isBlank()) {
            existingUser.setName(updateUserDTO.name());
        }

        if (updateUserDTO.email() != null && !updateUserDTO.email().isBlank()) {
            existingUser.setEmail(updateUserDTO.email());
        }

        if (updateUserDTO.password() != null && !updateUserDTO.password().isBlank()) {
            existingUser.setPassword(updateUserDTO.password());
        }

        User updatedUser = userRepository.save(existingUser);

        log.info("User updated successfully with ID: {}", updatedUser.getId());

        return UserResponseDTO.builder()
                .id(updatedUser.getId())
                .name(updatedUser.getName())
                .email(updatedUser.getEmail())
                .build();
    }

    public void deleteUserById(UUID id) {
        log.info("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            log.error("User not found with ID: {}", id);
            throw new ApiException("User not found with id: " + id);
        }
        
        userRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
    }
}
