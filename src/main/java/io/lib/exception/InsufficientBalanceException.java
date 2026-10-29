package io.lib.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends RuntimeException {
    private final BigDecimal currentBalance;

    public InsufficientBalanceException(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance == null ? BigDecimal.ZERO : currentBalance;
    }
}
