package io.customer.controller.external;


import io.customer.entity.Customer;
import io.customer.service.CustomerAuthService;
import io.customer.view.CustomerView;
import io.lib.view.EntityApiResponse;
import io.userauthentication.controller.AbstractAuthController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.lib.service.SystemConfig.CUSTOMER_USER_BASE_URL;


@RestController
@RequestMapping(CUSTOMER_USER_BASE_URL + "/auth")
public class CustomerAuthController extends AbstractAuthController<Customer> {
    private CustomerAuthService customerAuthService;


    @GetMapping("fetch")
    public EntityApiResponse<CustomerView> fetch(Authentication auth) {
        var user = customerAuthService.findByEntityId(auth.getName());
        return new EntityApiResponse<>(new CustomerView(user));
    }

    @Override
    protected CustomerAuthService getAuthService() {
        return customerAuthService;
    }

    @Autowired
    public void setCustomerAuthService(CustomerAuthService service) {
        this.customerAuthService = service;
    }
}
