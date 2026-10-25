package com.minishop.backend.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductVariantRequest(
        @Size(max = 50, message = "Size must be 50 characters or fewer")
        String size,
        @Size(max = 50, message = "Color must be 50 characters or fewer")
        String color,
        @NotBlank(message = "Variant SKU is required")
        @Size(max = 100, message = "Variant SKU must be 100 characters or fewer")
        String sku,
        @NotNull(message = "Variant stock quantity is required")
        @Min(value = 0, message = "Variant stock quantity cannot be negative")
        Integer stockQuantity
) {
}
