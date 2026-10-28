package io.userpermission.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {UniqueAuthGroupNameValidator.class})
@Target( { ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueAuthGroupName {
    String message() default "Name already exists.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
