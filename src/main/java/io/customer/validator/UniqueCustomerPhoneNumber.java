package io.customer.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {
        UniqueCustomerPhoneNumberValidator.class
})
@Target( { ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueCustomerPhoneNumber {
    String message() default "error.user.phone.number.exits";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
