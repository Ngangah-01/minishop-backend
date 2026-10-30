package io.productcatalog.validator;

import io.lib.service.BaseJpaRepoReadService;
import io.productcatalog.entity.ProductCategory;
import io.productcatalog.repository.ProductCategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

public class UniqueProductCategoryNameValidator extends BaseJpaRepoReadService<ProductCategory, ProductCategoryRepository> implements ConstraintValidator<UniqueProductCategoryName, String> {
    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if(name == null){
            return true;
        }

        String sanitizedName = WordUtils.capitalize(StringUtils.trimToEmpty(name));

        var spec = repository.notDeleted()
                .and(repository.nameIs(sanitizedName));

        return !repository.exists(spec);
    }
}
