package io.internaluser.validator;

import io.internaluser.entity.InternalUser;
import io.internaluser.repository.InternalUserRepository;
import io.lib.service.BaseJpaRepoEditService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

class InternalUserExistsSingleIdValidator extends BaseJpaRepoEditService<InternalUser, InternalUserRepository>
        implements ConstraintValidator<InternalUserExists, String> {

    @Override
    public boolean isValid(String id, ConstraintValidatorContext constraintValidatorContext) {
        return existsByEntityId(id);
    }
}

