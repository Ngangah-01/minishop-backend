package com.minishop.backend.auth.dto;

import com.minishop.backend.user.dto.UserResponse;

public record AuthResponse(

        String token,
        UserResponse user
) {
}
