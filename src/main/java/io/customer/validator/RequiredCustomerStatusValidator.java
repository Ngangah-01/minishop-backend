package io.customer.validator;

import io.customer.service.CustomerReadService;
import io.user.entity.UserStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
class RequiredCustomerStatusValidator
        implements ConstraintValidator<RequiredCustomerStatus, String> {
    private List<UserStatus> statuses;

    private CustomerReadService customerReadService;

    @Override
    public void initialize(RequiredCustomerStatus constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.statuses = List.of(constraintAnnotation.statuses());
    }

    @Override
    public boolean isValid(String id, ConstraintValidatorContext constraintValidatorContext) {
        var user = customerReadService.findByEntityId(id);
        return statuses.contains(user.getUserStatus());
    }

    @Autowired
    public void setCustomerReadService(CustomerReadService service) {
        this.customerReadService = service;
    }
}
