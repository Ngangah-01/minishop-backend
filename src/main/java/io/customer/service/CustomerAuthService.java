package io.customer.service;

import io.customer.entity.Customer;
import io.customer.repository.CustomerRepository;
import io.user.entity.UserType;
import io.user.service.UserAuthService;
import org.springframework.stereotype.Service;

@Service
public class CustomerAuthService extends UserAuthService<Customer, CustomerRepository> {

    @Override
    public UserType getUserType() {
        return UserType.CUSTOMER_USER;
    }
}
