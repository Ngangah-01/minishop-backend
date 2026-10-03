package com.minishop.backend.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Product name is required")
        String name,
        @NotBlank(message = "Product description is required")
        String description,
        @NotNull(message = "Product price is required")
        @Min(value = 0, message = "Price cannot be negative")
        BigDecimal price,
        @NotNull(message = "Product category is required")
        Long categoryId,
        @NotNull(message = "Product stock quantity is required")
        @Min(value = 0, message = "Stock quantity cannot be negative")
        Integer stockQuantity,
        @NotBlank(message = "Product SKU is required")
        String sku
) { // A concise, immutable data holder. Perfect fit for a DTO, just data, no behavior.
}
