package io.internaluser.form;

import io.internaluser.validator.RequiredInternalUserStatus;
import io.lib.form.SessionUserIdForm;
import io.user.entity.UserStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalUserIdForm extends SessionUserIdForm {
    @NotBlank(message = "error.invalid.user.id")
    @RequiredInternalUserStatus(
            statuses = {UserStatus.ACTIVE},
            message = "error.operation.not.allowed"
    )
    private String internalUserId;
}
