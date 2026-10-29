package io.lib.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = TodayOrFutureDateValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TodayOrFutureDate {
    String message() default "Date must be future or current.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
