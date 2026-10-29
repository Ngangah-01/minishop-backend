package io.userauthentication.form;

import io.lib.service.FormatUtil;
import io.userauthentication.entity.UsernameType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UsernameTypeForm extends UsernameForm {
    @NotNull(message = "error.invalid.username.type")
    private UsernameType usernameType;

    public String getAnonymisedUsername() {
        return switch (usernameType){
            case EMAIL -> FormatUtil.maskEmail(getUsername());
            case PHONE_NUMBER -> getUsername();
        };
    }
}
