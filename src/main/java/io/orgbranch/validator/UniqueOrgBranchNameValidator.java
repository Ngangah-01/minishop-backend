package io.orgbranch.validator;

import io.lib.service.BaseJpaRepoReadService;
import io.orgbranch.entity.OrgBranch;
import io.orgbranch.repository.OrgBranchRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UniqueOrgBranchNameValidator extends BaseJpaRepoReadService<OrgBranch, OrgBranchRepository>
        implements ConstraintValidator<UniqueOrgBranchName, String> {

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        String sanitizedName = WordUtils.capitalize(StringUtils.trimToEmpty(name));

        Specification<OrgBranch> spec = repository.notDeleted()
                .and(repository.nameIs(sanitizedName));

        return !repository.exists(spec);
    }
}
