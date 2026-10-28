package io.product.dto;

import io.product.ProductVariant;

public record ProductVariantResponse(
        Long id,
        String size,
        String color,
        String sku,
        Integer stockQuantity
) {
    public static ProductVariantResponse from(ProductVariant variant) {
        return new ProductVariantResponse(
                variant.getId(),
                variant.getSize(),
                variant.getColor(),
                variant.getSku(),
                variant.getStockQuantity()
        );
    }
}
