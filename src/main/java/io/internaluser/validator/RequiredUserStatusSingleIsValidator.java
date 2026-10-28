package io.internaluser.validator;

import io.internaluser.entity.InternalUser;
import io.internaluser.service.InternalUserReadService;
import io.user.entity.UserStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
class RequiredUserStatusSingleIsValidator
        implements ConstraintValidator<RequiredInternalUserStatus, String> {
    private List<UserStatus> statuses;

    private InternalUserReadService internalUserReadService;

    @Override
    public void initialize(RequiredInternalUserStatus constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.statuses = List.of(constraintAnnotation.statuses());
    }

    @Override
    public boolean isValid(String internalUserId, ConstraintValidatorContext constraintValidatorContext) {
        InternalUser user = internalUserReadService.findByEntityId(internalUserId);
        return this.statuses.contains(user.getUserStatus());
    }

    @Autowired
    public void setInternalUserReadService(InternalUserReadService internalUserReadService) {
        this.internalUserReadService = internalUserReadService;
    }
}
