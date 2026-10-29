package io.customer.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {
    UniqueCustomerTaxPinValidator.class
})
@Target( { ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueCustomerTaxPin {
    String message() default "error.tax.pin.exits";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
