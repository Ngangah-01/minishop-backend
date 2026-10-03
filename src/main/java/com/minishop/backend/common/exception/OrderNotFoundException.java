package com.minishop.backend.common.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) { super("Order not found: " + id); }
}