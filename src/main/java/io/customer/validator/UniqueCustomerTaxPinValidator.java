package io.customer.validator;

import io.customer.entity.Customer;
import io.customer.repository.CustomerRepository;
import io.lib.service.BaseJpaRepoReadService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
class UniqueCustomerTaxPinValidator extends BaseJpaRepoReadService<Customer, CustomerRepository>
        implements ConstraintValidator<UniqueCustomerTaxPin, String> {

    @Override
    public boolean isValid(String taxPin, ConstraintValidatorContext constraintValidatorContext) {
        if(StringUtils.isBlank(taxPin)){
            return true;
        }
        var spec = repository.notDeleted()
            .and(repository.taxPinIs(taxPin));
        return StringUtils.isBlank(taxPin) || !repository.exists(spec);
    }
}

