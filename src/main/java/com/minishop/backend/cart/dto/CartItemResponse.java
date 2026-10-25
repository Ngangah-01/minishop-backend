package com.minishop.backend.cart.dto;

import com.minishop.backend.product.Product;
import com.minishop.backend.cart.CartItem;

import java.math.BigDecimal;

public record CartItemResponse(
        Long productId,
        String productName,
        Long variantId,
        String size,
        String color,
        String sku,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal subtotal
) {

    public static CartItemResponse from(CartItem item) {
        Product product = item.getProduct();
        Integer quantity = item.getQuantity();
        BigDecimal subtotal =
                product.getPrice()
                        .multiply(BigDecimal.valueOf(quantity));

        return new CartItemResponse(
                product.getId(),
                product.getName(),
                item.getVariant() == null ? null : item.getVariant().getId(),
                item.getVariant() == null ? null : item.getVariant().getSize(),
                item.getVariant() == null ? null : item.getVariant().getColor(),
                item.getVariant() == null ? null : item.getVariant().getSku(),
                product.getPrice(),
                quantity,
                subtotal
        );
    }
}
