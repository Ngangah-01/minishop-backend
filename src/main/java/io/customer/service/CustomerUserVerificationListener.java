package io.customer.service;

import io.customer.entity.Customer;
import io.customer.repository.CustomerRepository;
import io.user.service.UserVerificationCodeListener;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserVerificationListener
        extends UserVerificationCodeListener<Customer, CustomerRepository> {
}
