package io.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.category.dto.CategoryResponse;
import io.product.Product;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        CategoryResponse category,
        Integer stockQuantity,
        String sku,
        List<ProductImageResponse> images,
        List<ProductVariantResponse> variants,
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
                product.getImages().stream().map(ProductImageResponse::from).toList(),
                product.getVariants().stream().map(ProductVariantResponse::from).toList(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
