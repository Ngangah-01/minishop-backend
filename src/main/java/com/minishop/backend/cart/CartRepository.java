package com.minishop.backend.cart;

import com.minishop.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository <Cart, Long> {


        Optional<Cart> findByUser(User user);

        Optional<Cart> findByUserId(Long userId);

        boolean existsByUserId(Long userId);
    }


