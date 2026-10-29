package io.customer.form;

import io.customer.validator.UniqueCustomerEmail;
import io.customer.validator.UniqueCustomerPhoneNumber;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRegistrationForm extends CustomerUpdateForm {

    @UniqueCustomerEmail
    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @UniqueCustomerPhoneNumber
    @Override
    public String getPhoneNumber() {
        return super.getPhoneNumber();
    }
}
