package io.user.dto;

import io.user.Role;
import io.user.User;

import java.time.Instant;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role,
        Instant createdAt,
        Instant updatedAt

) {
    // Factory method to create a UserResponse from a User entity
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
