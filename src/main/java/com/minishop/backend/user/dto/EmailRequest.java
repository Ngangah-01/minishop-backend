package com.minishop.backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailRequest(

        @NotBlank(message = "Email is required")
        @Email(message = "Enter avalid email address")
        String email
) {
}
