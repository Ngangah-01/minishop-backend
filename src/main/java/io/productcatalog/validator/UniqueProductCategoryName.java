package io.productcatalog.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {UniqueProductCategoryNameValidator.class})
@Target( { ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueProductCategoryName {
    String message() default "error.duplicate.product.category.name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
