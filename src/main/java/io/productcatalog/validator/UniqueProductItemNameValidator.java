package io.productcatalog.validator;

import io.lib.service.BaseJpaRepoReadService;
import io.productcatalog.entity.ProductItem;
import io.productcatalog.repository.ProductItemRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.stereotype.Service;

@Service
class UniqueProductItemNameValidator extends BaseJpaRepoReadService<ProductItem, ProductItemRepository> implements ConstraintValidator<UniqueProductItemName, String>{
    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        String sanitizedName = WordUtils.capitalize(StringUtils.trimToEmpty(name));

        var specification = repository.notDeleted()
                .and(repository.nameIs(sanitizedName));

        return !repository.exists(specification);
    }
}
