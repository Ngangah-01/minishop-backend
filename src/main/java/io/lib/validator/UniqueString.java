package io.lib.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueStringValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueString {
    String message() default "Input must be unique.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

