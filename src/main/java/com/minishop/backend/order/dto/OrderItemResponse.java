package com.minishop.backend.order.dto;

import com.minishop.backend.order.OrderItem;
import java.math.BigDecimal;

public record OrderItemResponse(
        Long productId,
        String productName,
        String variantSize,
        String variantColor,
        String variantSku,
        Integer quantity,
        BigDecimal priceAtPurchase,
        BigDecimal subtotal
) {
    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getVariantSize(),
                item.getVariantColor(),
                item.getVariantSku(),
                item.getQuantity(),
                item.getPriceAtPurchase(),
                item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
    }
}