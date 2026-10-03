package com.minishop.backend.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(

        @NotBlank(message = "Category name is required")
        @Size(
                max = 50,
                message = "Category name cannot exceed 50 characters"
        )
        String name,

        @Size(
                max = 100,
                message = "Category description cannot exceed 100 characters"
        )
        String description
) {
}