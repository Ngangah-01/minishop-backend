package io.customer.entity;

import io.user.entity.User;
import io.user.entity.UserType;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Customer extends User {
    @Override
    public UserType getUserType() {
        return UserType.CUSTOMER_USER;
    }

    @Override
    public String getUserTypeStr() {
        return "Customer";
    }
}
