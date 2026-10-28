package io.internaluser.validator;

import io.internaluser.service.InternalUserReadService;
import io.user.entity.UserStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
class RequiredUserStatusMultipleIdsValidator
        implements ConstraintValidator<RequiredInternalUserStatus, List<String>> {
    private List<UserStatus> statuses;

    private InternalUserReadService internalUserReadService;

    @Override
    public void initialize(RequiredInternalUserStatus constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.statuses = List.of(constraintAnnotation.statuses());
    }

    @Override
    public boolean isValid(List<String> ids, ConstraintValidatorContext context) {
        var users = internalUserReadService.findByIds(ids);
        return users.stream().allMatch(user -> this.statuses.contains(user.getUserStatus()));
    }

    @Autowired
    public void setInternalUserReadService(InternalUserReadService service) {
        this.internalUserReadService = service;
    }
}
