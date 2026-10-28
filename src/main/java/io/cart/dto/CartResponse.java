package io.cart.dto;

import io.cart.Cart;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record CartResponse(
        List<CartItemResponse> items,
        Integer totalItems,
        BigDecimal totalAmount,
        Instant createdAt,
        Instant updatedAt
) {

    public static CartResponse from(Cart cart) {

        List<CartItemResponse> items =
                cart.getItems()
                        .stream()
                        .map(CartItemResponse::from)
                        .toList();

        Integer totalItems =
                items.stream()
                        .mapToInt(CartItemResponse::quantity)
                        .sum();

        BigDecimal totalAmount =
                items.stream()
                        .map(CartItemResponse::subtotal)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        return new CartResponse(
                items,
                totalItems,
                totalAmount,
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }
}