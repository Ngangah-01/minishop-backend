package io.userpermission.validator;


import io.lib.service.BaseJpaRepoEditService;
import io.userpermission.model.AuthGroup;
import io.userpermission.repository.AuthGroupRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Service;

@Service
public class AuthGroupIsEditableValidator extends BaseJpaRepoEditService<AuthGroup, AuthGroupRepository>
        implements ConstraintValidator<AuthGroupIsEditable, String> {

    @Override
    public boolean isValid(String id, ConstraintValidatorContext constraintValidatorContext) {
        return findByEntityId(id).isEditable();
    }
}
