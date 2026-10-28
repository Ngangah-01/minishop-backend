package io.auth.dto;

import io.user.UserRepository;
import io.validation.UniqueEmailValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class RegisterRequestValidationTest {

    private static Validator validator;
    private static ValidatorFactory validatorFactory;

    @BeforeAll
    static void setUpValidator() {
        UserRepository userRepository = mock(UserRepository.class);
        ConstraintValidatorFactory constraintValidatorFactory = new ConstraintValidatorFactory() {
            @Override
            public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
                if (key == UniqueEmailValidator.class) {
                    return key.cast(new UniqueEmailValidator(userRepository));
                }
                try {
                    return key.getDeclaredConstructor().newInstance();
                } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException("Could not create constraint validator", e);
                }
            }

            @Override
            public void releaseInstance(ConstraintValidator<?, ?> instance) {
            }
        };
        validatorFactory = Validation.byDefaultProvider()
                .configure()
                .constraintValidatorFactory(constraintValidatorFactory)
                .buildValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidatorFactory() throws Exception {
        validatorFactory.close();
    }

    @Test
    void acceptsRegistrationWithFourRequiredFields() {
        RegisterRequest request = new RegisterRequest(
                "Ada",
                "Lovelace",
                "ada@example.com",
                "password123"
        );

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void rejectsMissingOrBlankFirstAndLastNames() {
        RegisterRequest request = new RegisterRequest(
                " ",
                null,
                "ada@example.com",
                "password123"
        );

        var violations = validator.validate(request);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }
}
