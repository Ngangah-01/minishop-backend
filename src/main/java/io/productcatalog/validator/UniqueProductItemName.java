package io.productcatalog.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {UniqueProductItemNameValidator.class})
@Target( { ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueProductItemName {
    String message() default "error.duplicate.name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
