package io.userauthentication.validator;


import io.lib.service.BaseJpaRepoEditService;
import io.userauthentication.entity.VerificationCode;
import io.userauthentication.repository.VerificationCodeRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Service;


@Service
class VerificationCodeExistsValidator extends BaseJpaRepoEditService<VerificationCode, VerificationCodeRepository>
        implements ConstraintValidator<VerificationCodeExists, String> {

    @Override
    public boolean isValid(String verificationCodeId, ConstraintValidatorContext constraintValidatorContext) {
        return existsByEntityId(verificationCodeId);
    }
}
