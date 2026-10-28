package io.customer.service;

import io.customer.entity.Customer;
import io.customer.form.CustomerUpdateForm;
import io.customer.repository.CustomerRepository;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.user.service.BaseUserEditService;
import org.springframework.stereotype.Service;

@Service
public class CustomerEditService extends BaseUserEditService<Customer, CustomerRepository> {

    public Customer register(CustomerUpdateForm form){
        var customer = super.register(form);
        customer.setNationalId(form.getNationalId());
        customer.setTaxPin(form.getTaxPin());
        customer = save(customer, form.getSessionUserId());
        return customer;
    }

    public Customer edit(String customerId, CustomerUpdateForm form){
        checkTaxPinExists(form.getTaxPin(), customerId);
        checkNationalIdExists(form.getNationalId(), customerId);

        var customer = super.edit(customerId, form);
        customer.setNationalId(form.getNationalId());
        customer.setTaxPin(form.getTaxPin());
        customer = save(customer, form.getSessionUserId());
        return customer;
    }

    private void  checkTaxPinExists(String taxPin, String customerId){
        var spec = repository.notDeleted()
            .and(repository.taxPinIs(taxPin))
            .and(repository.entityIdNot(customerId));

        boolean exists = repository.exists(spec);
        if (exists) {
            throw new CommonRuntimeException(ExceptionType.ALREADY_EXISTS, "error.tax.pin.exits");
        }
    }

    private void  checkNationalIdExists(String nationalId, String customerId){
        var spec = repository.notDeleted()
            .and(repository.nationalIdIs(nationalId))
            .and(repository.entityIdNot(customerId));

        boolean exists = repository.exists(spec);
        if (exists) {
            throw new CommonRuntimeException(ExceptionType.ALREADY_EXISTS, "error.national.id.exits");
        }
    }

    @Override
    protected Customer getNewUser() {
        return new Customer();
    }
}
