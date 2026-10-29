package io.user.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordForm {
    @NotBlank(message = "error.invalid.password")
    private String password;
}
