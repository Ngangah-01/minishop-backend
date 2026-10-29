package io.lib.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;


public class TodayOrFutureDateValidator
        implements ConstraintValidator<TodayOrFutureDate, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime inputDate, ConstraintValidatorContext cxt) {

        if(inputDate == null) {
            return true;
        }

        LocalDateTime currentDate = LocalDateTime.now();
        return inputDate.equals(currentDate) || inputDate.isAfter(currentDate);
    }
}
