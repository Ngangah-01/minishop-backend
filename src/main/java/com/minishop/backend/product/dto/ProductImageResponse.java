package com.minishop.backend.product.dto;

import com.minishop.backend.product.ProductImage;

public record ProductImageResponse(Long id, String url, Integer displayOrder) {
    public static ProductImageResponse from(ProductImage image) {
        return new ProductImageResponse(image.getId(), image.getUrl(), image.getDisplayOrder());
    }
}
