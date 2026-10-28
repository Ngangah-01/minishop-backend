package io.orgbranch.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {UniqueOrgBranchNameValidator.class})
@Target( { ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueOrgBranchName {
    String message() default "Name already exists.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}