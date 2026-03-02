package com.xndfinance.controller;

import com.xndfinance.dto.user.CreateUserRequestDTO;
import com.xndfinance.dto.user.UpdateUserDTO;
import com.xndfinance.dto.user.UserResponseDTO;
import com.xndfinance.model.User;
import com.xndfinance.service.user.UserAuthService;
import com.xndfinance.service.user.UserQueryService;
import com.xndfinance.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserQueryService userQueryService;
    private final UserAuthService userAuthService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserRequestDTO userDTO) {
        User createdUser = userService.createUser(userDTO);

        UserResponseDTO response = new UserResponseDTO(
                createdUser.getId(),
                createdUser.getName(),
                createdUser.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestParam String email, @RequestParam String password) {
        log.info("Login attempt for email: {}", email);
        UserResponseDTO authenticatedUser = userAuthService.login(email, password);
        return ResponseEntity.ok(authenticatedUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.debug("Fetching all users");
        List<User> users = userQueryService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        log.debug("Fetching user with ID: {}", id);
        return userQueryService.findById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        log.debug("Fetching user with email: {}", email);
        return userQueryService.findByEmail(email)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @RequestBody UpdateUserDTO userDetails) {
        log.info("Updating user with ID: {}", id);
        UserResponseDTO updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        log.info("Deleting user with ID: {}", id);
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable UUID id) {
        log.debug("Checking if user exists with ID: {}", id);
        boolean exists = userQueryService.existsById(id);
        return ResponseEntity.ok(exists);
    }
}