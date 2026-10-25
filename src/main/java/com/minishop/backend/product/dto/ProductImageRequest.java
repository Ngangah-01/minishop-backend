package com.minishop.backend.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductImageRequest(
        @NotBlank(message = "Image URL is required")
        @Size(max = 2048, message = "Image URL must be 2048 characters or fewer")
        @Pattern(regexp = "https?://.+", message = "Image URL must use HTTP or HTTPS")
        String url
) {
}
