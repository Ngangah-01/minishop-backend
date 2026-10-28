package io.order;

import io.common.response.ApiResponse;
import io.order.dto.OrderResponse;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /*
     * CREATE ORDER FROM CURRENT USER'S CART
     *
     * No @RequestBody.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder() {

        OrderResponse order = orderService.placeOrder();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order created successfully",
                        order
                )
        );
    }

    /*
     * GET ALL ORDERS BELONGING TO CURRENT USER
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrders() {

        List<OrderResponse> orders = orderService.getMyOrders();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Orders retrieved successfully",
                        orders
                )
        );
    }

    /*
     * GET ONE ORDER BELONGING TO CURRENT USER
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getMyOrder(
            @PathVariable @Positive Long orderId
    ) {

        OrderResponse order = orderService.getMyOrder(orderId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order retrieved successfully",
                        order
                )
        );
    }

    /*
     * CANCEL ORDER
     */
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @PathVariable @Positive Long orderId
    ) {

        OrderResponse order = orderService.cancelOrder(orderId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order cancelled successfully",
                        order
                )
        );
    }
}