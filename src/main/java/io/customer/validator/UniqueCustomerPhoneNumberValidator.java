package io.customer.validator;

import io.customer.entity.Customer;
import io.customer.repository.CustomerRepository;
import io.user.service.BaseUserReadService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
class UniqueCustomerPhoneNumberValidator extends BaseUserReadService<Customer, CustomerRepository>
        implements ConstraintValidator<UniqueCustomerPhoneNumber, String> {

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.isBlank(phoneNumber) || !existsByPhoneNumber(phoneNumber);
    }
}

