package com.xndfinance.service.user;

import com.xndfinance.dto.user.CreateUserRequestDTO;
import com.xndfinance.dto.user.UpdateUserDTO;
import com.xndfinance.dto.user.UserResponseDTO;
import com.xndfinance.exception.ApiException;
import com.xndfinance.model.User;
import com.xndfinance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .email("test@test.com")
                .password("password123")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateUserSuccess() {
        CreateUserRequestDTO newUser = new CreateUserRequestDTO(
                "New Test",
                "newtest@test.com",
                "password123"
        );

        when(userRepository.findByEmail(newUser.email())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User createdUser = userService.createUser(newUser);
        assertNotNull(createdUser);
        assertNotNull(createdUser.getCreatedAt());
        assertEquals(testUser.getId(), createdUser.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUserFailEmailAlreadyExists() {
        CreateUserRequestDTO existingUser = new CreateUserRequestDTO(
                testUser.getName(),
                testUser.getEmail(),
                testUser.getPassword()
        );
        
        when(userRepository.findByEmail(existingUser.email())).thenReturn(Optional.of(testUser));

        assertThrows(ApiException.class, () -> userService.createUser(existingUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUserSuccess() {
        UpdateUserDTO updatedDetails = new UpdateUserDTO(
                "Updated Name",
                "updated@test.com",
                "newpassword"
        );

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userRepository.save(testUser)).thenReturn(testUser);

        UserResponseDTO updatedUser = userService.updateUser(testUser.getId(), updatedDetails);
        assertNotNull(updatedUser);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testUpdateUserFailIdNotFound() {
        UpdateUserDTO updateDetails = new UpdateUserDTO(
                "Updated Name",
                "updated@test.com",
                "newpassword"
        );
        
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> userService.updateUser(testUser.getId(), updateDetails));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUserSuccess() {
        when(userRepository.existsById(testUser.getId())).thenReturn(true);

        userService.deleteUserById(testUser.getId());

        verify(userRepository, times(1)).existsById(testUser.getId());
        verify(userRepository, times(1)).deleteById(testUser.getId());
    }

    @Test
    void testDeleteUserFailIdNotFound() {
        when(userRepository.existsById(testUser.getId())).thenReturn(false);

        assertThrows(ApiException.class, () -> userService.deleteUserById(testUser.getId()));
        verify(userRepository, never()).deleteById(testUser.getId());
    }
}
