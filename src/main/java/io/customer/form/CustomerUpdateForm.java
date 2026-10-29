package io.customer.form;

import io.user.form.UserEditForm;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerUpdateForm extends UserEditForm {

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @NotBlank(message = "error.invalid.phone.number")
    @Override
    public String getPhoneNumber() {
        return super.getPhoneNumber();
    }
}
