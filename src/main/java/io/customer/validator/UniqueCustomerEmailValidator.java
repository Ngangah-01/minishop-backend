package io.customer.validator;

import io.customer.entity.Customer;
import io.customer.repository.CustomerRepository;
import io.user.service.BaseUserReadService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
class UniqueCustomerEmailValidator extends BaseUserReadService<Customer, CustomerRepository>
        implements ConstraintValidator<UniqueCustomerEmail, String> {

    @Override
    public boolean isValid(String emailAddress, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.isBlank(emailAddress) || !existsByEmail(emailAddress);
    }
}

