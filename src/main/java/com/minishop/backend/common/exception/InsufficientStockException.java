package com.minishop.backend.common.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}

//import lombok.Getter;
//
//@Getter
//public class InsufficientStockException extends RuntimeException {
//
//    private final String productName;
//    private final Integer requestedQuantity;
//    private final Integer availableStock;
//
//    public InsufficientStockException(String productName, Integer requestedQuantity, Integer availableStock) {
//        super(String.format("Insufficient stock for '%s'. Requested: %d, Available: %d",
//                productName, requestedQuantity, availableStock));
//        this.productName = productName;
//        this.requestedQuantity = requestedQuantity;
//        this.availableStock = availableStock;
//    }
//
//    public InsufficientStockException(String message) {
//        super(message);
//        this.productName = null;
//        this.requestedQuantity = null;
//        this.availableStock = null;
//    }
//
//}
