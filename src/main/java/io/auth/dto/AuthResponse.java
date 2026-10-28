package io.auth.dto;

import io.user.dto.UserResponse;

public record AuthResponse(

        String token,
        UserResponse user
) {
}
