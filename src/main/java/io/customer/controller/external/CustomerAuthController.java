package io.customer.controller.external;

import io.customer.entity.Customer;
import io.customer.form.CustomerSelfRegistrationForm;
import io.customer.service.CustomerAuthService;
import io.customer.service.CustomerEditService;
import io.customer.view.CustomerView;
import io.lib.service.Message;
import io.lib.view.EntityApiResponse;
import io.userauthentication.controller.AbstractAuthController;
import io.userauthentication.view.AuthView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static io.lib.service.SystemConfig.CUSTOMER_USER_BASE_URL;


@RestController
@RequestMapping(CUSTOMER_USER_BASE_URL + "/auth")
public class CustomerAuthController extends AbstractAuthController<Customer> {
    private CustomerAuthService customerAuthService;
    private CustomerEditService customerEditService;

    @PostMapping("register")
    public EntityApiResponse<AuthView> registerCustomerAccount(
        @RequestBody
        @Valid
        CustomerSelfRegistrationForm form,
        Locale locale
    ){
        var customer = customerEditService.register(form);
        var view = customerAuthService.onUserAuthentication(customer);
        return new EntityApiResponse<>(
            Message.get("user.registration.success", locale),
            view
        );
    }

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

    @Autowired
    public void setCustomerEditService(CustomerEditService service) {
        this.customerEditService = service;
    }
}
