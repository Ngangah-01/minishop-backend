package io.customer.service;

import io.customer.entity.Customer;
import io.customer.form.CustomerRegistrationForm;
import io.customer.form.CustomerSelfRegistrationForm;
import io.customer.form.CustomerUpdateForm;
import io.customer.repository.CustomerRepository;
import io.user.entity.UserStatus;
import io.user.service.BaseUserEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerEditService extends BaseUserEditService<Customer, CustomerRepository> {
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Customer register(CustomerSelfRegistrationForm form){
        var customer = super.register(form);

        var hashedPassword = bCryptPasswordEncoder.encode(form.getPassword());
        customer.setPassword(hashedPassword);
        customer.setUserStatus(UserStatus.ACTIVE);
        save(customer, form.getSessionUserId());

        return customer;
    }

    public Customer register(CustomerRegistrationForm form){
        return super.register(form);
    }

    public Customer edit(String customerId, CustomerUpdateForm form){
        var customer = super.edit(customerId, form);
        customer = save(customer, form.getSessionUserId());
        return customer;
    }

    @Override
    protected Customer getNewUser() {
        return new Customer();
    }

    @Autowired
    public void setBCryptPasswordEncoder(BCryptPasswordEncoder encoder) {
        this.bCryptPasswordEncoder = encoder;
    }
}
