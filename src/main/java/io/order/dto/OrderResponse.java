package io.order.dto;

import io.order.Order;
import io.order.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        OrderStatus status,
        Instant createdAt,
        List<OrderItemResponse> items,
        BigDecimal total
) {
    public static OrderResponse from(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(OrderItemResponse::from)
                .toList();

        BigDecimal total = items.stream()
                .map(OrderItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrderResponse(
                order.getId(),
                order.getStatus(),
                order.getCreatedAt(),
                items,
                total
        );
    }
}