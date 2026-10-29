package io.customer.form;

import io.user.form.UserEditForm;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerUpdateForm extends UserEditForm {
    @NotBlank(message = "error.invalid.national.id")
    private String nationalId;

    @NotBlank(message = "error.invalid.tax.pin")
    private String taxPin;

    @NotBlank(message = "error.invalid.email")
    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @NotBlank(message = "error.invalid.phone.number")
    @Override
    public String getPhoneNumber() {
        return super.getPhoneNumber();
    }

    public String getTaxPin(){
        return this.taxPin.trim().toUpperCase();
    }
}
