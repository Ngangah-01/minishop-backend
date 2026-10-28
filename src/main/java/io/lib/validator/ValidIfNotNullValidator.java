package io.lib.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;

class ValidIfNotNullValidator implements ConstraintValidator<ValidIfNotNull, Object> {

    @Autowired
    private Validator validator;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return validator.validate(value).isEmpty();
    }
}
