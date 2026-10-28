package io.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

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
        @Size(max = 100, message = "Product SKU must be 100 characters or fewer")
        String sku,
        List<@NotBlank(message = "Image URL is required")
             @Size(max = 2048, message = "Image URL must be 2048 characters or fewer")
             @Pattern(regexp = "https?://.+", message = "Image URL must use HTTP or HTTPS")
             String> imageUrls
) { // A concise, immutable data holder. Perfect fit for a DTO, just data, no behavior.
}
