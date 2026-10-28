package io.lib.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UniqueStringValidator
        implements ConstraintValidator<UniqueString, Collection<String>> {

    @Override
    public boolean isValid(Collection<String> inputStr, ConstraintValidatorContext cxt) {
        Set<String> stringSet = new HashSet<>(inputStr);
        return stringSet.size() == inputStr.size();
    }
}
