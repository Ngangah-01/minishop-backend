package com.minishop.backend.category.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.minishop.backend.category.Category;

import java.time.Instant;

public record CategoryResponse(
    Long id,
    String name,
    String description,
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "dd MMM yyyy, HH:mm:ss",
            timezone = "Africa/Nairobi"
    )
    Instant createdAt,
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "dd MMM yyyy, HH:mm:ss",
            timezone = "Africa/Nairobi"
    )
    Instant updatedAt
) {
    // Factory method to create a CategoryResponse from a Category entity
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
