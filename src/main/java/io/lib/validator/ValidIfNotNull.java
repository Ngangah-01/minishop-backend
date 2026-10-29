package io.lib.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {ValidIfNotNullValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIfNotNull {
    String message() default "Field validation failed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
