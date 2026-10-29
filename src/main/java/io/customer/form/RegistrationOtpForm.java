package io.customer.form;

import io.userauthentication.form.OtpForm;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegistrationOtpForm extends OtpForm {
    @NotBlank(message = "error.invalid.verification.code.id")
    private String verificationCodeId;
}
