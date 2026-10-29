package io.userpermission.validator;

import io.lib.service.BaseJpaRepoEditService;
import io.userpermission.model.AuthGroup;
import io.userpermission.repository.AuthGroupRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class UniqueAuthGroupNameValidator extends BaseJpaRepoEditService<AuthGroup, AuthGroupRepository>
        implements ConstraintValidator<UniqueAuthGroupName, String> {

    @Override
    public boolean isValid(String authGroupName, ConstraintValidatorContext constraintValidatorContext) {
        String cleanedAuthGroupName =  authGroupName.trim()
            .replaceAll(" +", " ")
            .toLowerCase();

        Specification<AuthGroup> spec = repository.notDeleted()
            .and(repository.nameIs(cleanedAuthGroupName));

        return !repository.exists(spec);
    }
}
