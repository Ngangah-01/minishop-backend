package io.customer.form;

import io.customer.validator.UniqueCustomerEmail;
import io.customer.validator.UniqueCustomerNationalId;
import io.customer.validator.UniqueCustomerPhoneNumber;
import io.customer.validator.UniqueCustomerTaxPin;
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

    @UniqueCustomerNationalId
    @Override
    public String getNationalId(){
        return super.getNationalId();
    }

    @UniqueCustomerTaxPin
    @Override
    public String getTaxPin() {
        return super.getTaxPin();
    }
}
