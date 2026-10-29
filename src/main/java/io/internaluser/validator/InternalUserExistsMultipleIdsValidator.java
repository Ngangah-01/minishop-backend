package io.internaluser.validator;

import io.internaluser.entity.InternalUser;
import io.internaluser.repository.InternalUserRepository;
import io.lib.service.BaseJpaRepoEditService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

class InternalUserExistsMultipleIdsValidator extends BaseJpaRepoEditService<InternalUser, InternalUserRepository>
        implements ConstraintValidator<InternalUserExists, List<String>> {

    @Override
    public boolean isValid(List<String> ids, ConstraintValidatorContext constraintValidatorContext) {
        return findByIds(ids).size() == ids.size();
    }
}

