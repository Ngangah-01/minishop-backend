package com.minishop.backend.user.dto;

import com.minishop.backend.user.Role;
import com.minishop.backend.user.User;

import java.time.Instant;

public record UserResponse(
        Long id,
        String name,
        String email,
        Role role,
        Instant createdAt,
        Instant updatedAt

) {
    // Factory method to create a UserResponse from a User entity
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
