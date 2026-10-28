package io.customer.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {
    UniqueCustomerEmailValidator.class
})
@Target( { ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueCustomerEmail {
    String message() default "error.user.email.exits";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
