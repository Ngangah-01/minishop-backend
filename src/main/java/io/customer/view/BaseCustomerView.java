package io.customer.view;

import io.customer.entity.Customer;
import io.user.view.UserView;

abstract class BaseCustomerView extends UserView<Customer> {

    protected BaseCustomerView(Customer entity) {
        super(entity);
    }

    public String getNationalId(){
        return entity.getNationalId();
    }

    public String getTaxPin(){
        return entity.getTaxPin();
    }

}
