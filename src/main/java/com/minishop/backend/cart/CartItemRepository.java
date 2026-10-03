package com.minishop.backend.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartIdAndProductId(
            Long cartId,
            Long productId
    );

    boolean existsByCartIdAndProductId(
            Long cartId,
            Long productId
    );

    void deleteByCartId(Long cartId);
}