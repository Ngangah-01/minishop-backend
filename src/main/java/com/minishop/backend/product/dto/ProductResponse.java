package com.minishop.backend.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.minishop.backend.category.dto.CategoryResponse;
import com.minishop.backend.product.Product;
import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        CategoryResponse category,
        Integer stockQuantity,
        String sku,
        @JsonFormat(shape = JsonFormat.Shape.STRING,
                    pattern = "yyyy-MM-dd HH:mm:ss",
                    timezone = "UTC")
        Instant createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd HH:mm:ss",
                timezone = "UTC")
        Instant updatedAt
) {
    // Factory method to create a ProductResponse from a Product entity
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                CategoryResponse.from(product.getCategory()),
                product.getStockQuantity(),
                product.getSku(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
