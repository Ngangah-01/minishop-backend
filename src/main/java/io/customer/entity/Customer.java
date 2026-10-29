package io.customer.entity;

import io.lib.service.FormatUtil;
import io.user.entity.User;
import io.user.entity.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Customer extends User {

    @Column(unique = true)
    private String taxPin;

    private Boolean taxPinVerified;

    @Column(unique = true)
    private String nationalId;

    private Boolean nationalIdVerified;

    private LocalDate dateOfBirth;

    private String bankAccountNumber;

    @Column(precision = FormatUtil.BIG_DECIMAL_PRECISION, scale = FormatUtil.BIG_DECIMAL_SCALE)
    private BigDecimal accountBalance;

    @Override
    public UserType getUserType() {
        return UserType.CUSTOMER_USER;
    }

    @Override
    public String getUserTypeStr() {
        return "Customer";
    }

    public BigDecimal getAccountBalance() {
        return accountBalance == null
                ? BigDecimal.ZERO
                : accountBalance;
    }
}
