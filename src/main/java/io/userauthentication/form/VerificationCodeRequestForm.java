package io.userauthentication.form;

import io.userauthentication.entity.VerificationCodeUse;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationCodeRequestForm extends UsernameTypeForm {
    @NotNull(message = "error.invalid.otp.use")
    private VerificationCodeUse verificationCodeUse;
}
