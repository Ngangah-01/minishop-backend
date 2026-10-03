package com.minishop.backend.cart.dto;

import com.minishop.backend.product.Product;

import java.math.BigDecimal;

public record CartItemResponse(
        Long productId,
        String productName,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal subtotal
) {

    public static CartItemResponse from(
            Product product,
            Integer quantity
    ) {
        BigDecimal subtotal =
                product.getPrice()
                        .multiply(BigDecimal.valueOf(quantity));

        return new CartItemResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                quantity,
                subtotal
        );
    }
}
