package io.userpermission.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {AuthGroupIdExistsValidator.class})
@Target( { ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthGroupIdExists {
    String message() default "Name already exists.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
