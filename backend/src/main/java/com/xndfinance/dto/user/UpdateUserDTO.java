package com.xndfinance.dto.user;

public record UpdateUserDTO(
        String name,
        String email,
        String password
) {
}
