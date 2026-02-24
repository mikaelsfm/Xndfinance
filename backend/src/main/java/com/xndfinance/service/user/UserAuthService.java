package com.xndfinance.service.user;

import com.xndfinance.dto.user.UserResponseDTO;
import com.xndfinance.exception.UnauthorizedException;
import com.xndfinance.model.User;
import com.xndfinance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository repository;

    public UserResponseDTO login(String email, String password) {

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password!"));

        if (!user.getPassword().equals(password)) {
            throw new UnauthorizedException("Invalid email or password!");
        }

        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

    }

}
