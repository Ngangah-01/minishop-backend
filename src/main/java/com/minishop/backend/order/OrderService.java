package com.minishop.backend.order;

import com.minishop.backend.cart.Cart;
import com.minishop.backend.cart.CartItem;
import com.minishop.backend.cart.CartRepository;
import com.minishop.backend.common.exception.EmptyCartException;
import com.minishop.backend.common.exception.InsufficientStockException;
import com.minishop.backend.common.exception.ResourceNotFoundException;
import com.minishop.backend.order.dto.OrderResponse;
import com.minishop.backend.product.Product;
import com.minishop.backend.security.CurrentUserProvider;
import com.minishop.backend.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CurrentUserProvider currentUserProvider;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository,
                        CurrentUserProvider currentUserProvider) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Transactional
    public OrderResponse placeOrder() {
        User user = currentUserProvider.getCurrentUser();

        Cart cart = cartRepository.findByUser(user).orElseThrow(EmptyCartException::new);
        if (cart.getItems().isEmpty()) {
            throw new EmptyCartException();
        }

        Order order = new Order(user);

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();
            int available = product.getStockQuantity();

            if (product.getStockQuantity() < quantity) {
                throw new InsufficientStockException(
                        "Not enough stock for '" + product.getName() +
                                "': requested " + quantity +
                                ", available " + available );
            }

            // No productRepository.save() needed: `product` is a managed entity
            // inside this transaction, so Hibernate detects the change ("dirty
            // checking") and writes the UPDATE automatically at commit.
            product.setStockQuantity(product.getStockQuantity() - quantity);

            order.addItem(new OrderItem(product, quantity));
        }

        Order saved = orderRepository.save(order);

        // orphanRemoval = true on Cart.items means clearing the list deletes the rows.
        cart.getItems().clear();

        return OrderResponse.from(saved);
    }

//    getting all orders for the current user
@Transactional(readOnly = true)
public List<OrderResponse> getMyOrders() {
    User user = currentUserProvider.getCurrentUser();
    return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
            .stream()
            .map(OrderResponse::from)
            .toList();
}

// getting one order
@Transactional(readOnly = true)
public OrderResponse getMyOrder(Long orderId) {

    User user = currentUserProvider.getCurrentUser();

    Order order = orderRepository
            .findByIdAndUser(orderId, user)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Order with id " + orderId + " not found"
                    )
            );

    return OrderResponse.from(order);
}
    @Transactional
    public OrderResponse cancelOrder(Long orderId) {

        User user = currentUserProvider.getCurrentUser();

        Order order = orderRepository
                .findByIdAndUser(orderId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order with id " + orderId + " not found"
                        )
                );

        /*
         * Prevent cancelling an order that is already cancelled.
         */
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Order has already been cancelled"
            );
        }

        /*
         * An order that has already been delivered should not
         * be cancelled through this endpoint.
         */
//        if (order.getStatus() == OrderStatus.DELIVERED) {
//            throw new IllegalStateException(
//                    "Delivered orders cannot be cancelled"
//            );
//        }

        /*
         * Restore the stock for every item in the order.
         */
        for (OrderItem orderItem : order.getItems()) {

            Product product = orderItem.getProduct();

            product.setStockQuantity(
                    product.getStockQuantity()
                            + orderItem.getQuantity()
            );
        }

        /*
         * Preserve the order as a historical record.
         */
        order.setStatus(OrderStatus.CANCELLED);

        return OrderResponse.from(order);
    }
}
