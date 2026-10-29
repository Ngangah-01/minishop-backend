package io.customer.validator;

import io.customer.entity.Customer;
import io.customer.repository.CustomerRepository;
import io.lib.service.BaseJpaRepoReadService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
class UniqueCustomerNationalIdValidator extends BaseJpaRepoReadService<Customer, CustomerRepository>
        implements ConstraintValidator<UniqueCustomerNationalId, String> {

    @Override
    public boolean isValid(String nationalId, ConstraintValidatorContext constraintValidatorContext) {
        var spec = repository.notDeleted()
                .and(repository.nationalIdIs(nationalId));

        return StringUtils.isBlank(nationalId) || !repository.exists(spec);
    }
}

