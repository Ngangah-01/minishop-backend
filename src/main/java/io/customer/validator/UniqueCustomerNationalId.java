package io.customer.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {
    UniqueCustomerNationalIdValidator.class
})
@Target( { ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueCustomerNationalId {
    String message() default "error.national.id.exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
