package io.internaluser.form;

import io.user.form.UserEditForm;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalUserEditForm extends UserEditForm {

    private String orgBranchId;

    @NotBlank(message = "error.invalid.email")
    @Override
    public String getEmail() {
        return super.getEmail();
    }

    private String orgDepartmentId;
}
