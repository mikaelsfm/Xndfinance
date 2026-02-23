package com.xndfinance.controller;

import com.xndfinance.model.User;
import com.xndfinance.service.user.UserAuthService;
import com.xndfinance.service.user.UserQueryService;
import com.xndfinance.service.user.UserService;
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

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Creating user with email: {}", user.getEmail());
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            log.error("Error creating user: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String email, @RequestParam String password) {
        log.info("Login attempt for email: {}", email);
        try {
            User authenticatedUser = userAuthService.login(email, password);
            return ResponseEntity.ok(authenticatedUser);
        } catch (RuntimeException e) {
            log.error("Login failed for email: {}", email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User userDetails) {
        log.info("Updating user with ID: {}", id);
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            log.error("Error updating user: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        log.info("Deleting user with ID: {}", id);
        try {
            userService.deleteUserById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting user: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable UUID id) {
        log.debug("Checking if user exists with ID: {}", id);
        boolean exists = userQueryService.existsById(id);
        return ResponseEntity.ok(exists);
    }
}