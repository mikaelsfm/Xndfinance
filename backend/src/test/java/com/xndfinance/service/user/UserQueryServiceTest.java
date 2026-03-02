package com.xndfinance.service.user;

import com.xndfinance.model.User;
import com.xndfinance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserQueryService userQueryService;

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
    void testFindUserByIdSuccess() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        Optional<User> userFound = userQueryService.findById(testUser.getId());

        assertNotNull(userFound);
        assertTrue(userFound.isPresent());
        assertEquals(testUser.getId(), userFound.get().getId());
        assertEquals(testUser.getName(), userFound.get().getName());
        assertEquals(testUser.getEmail(), userFound.get().getEmail());
        verify(userRepository, times(1)).findById(testUser.getId());
    }

    @Test
    void testFindUserByEmail() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        Optional<User> userFound = userQueryService.findByEmail(testUser.getEmail());

        assertNotNull(userFound);
        assertTrue(userFound.isPresent());
        assertEquals(testUser.getId(), userFound.get().getId());
        assertEquals(testUser.getName(), userFound.get().getName());
        assertEquals(testUser.getEmail(), userFound.get().getEmail());
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
    }

    @Test
    void testFindAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        List<User> users = userQueryService.findAll();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(testUser.getId(), users.get(0).getId());
        assertEquals(testUser.getName(), users.get(0).getName());
        assertEquals(testUser.getEmail(), users.get(0).getEmail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testExistsById() {
        when(userRepository.existsById(testUser.getId())).thenReturn(true);

        boolean exists = userQueryService.existsById(testUser.getId());

        assertTrue(exists);
        verify(userRepository, times(1)).existsById(testUser.getId());
    }
}