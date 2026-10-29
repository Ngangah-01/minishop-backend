package io.customer.form;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CustomerSelfRegistrationForm extends CustomerRegistrationForm {

    @NotBlank(message = "error.invalid.password")
    @Length(min = 5, message = "error.invalid.password")
    private String password;

    @NotNull(message = "error.invalid.phone.verification")
    private @Valid RegistrationOtpForm phoneVerificationCode;
}
