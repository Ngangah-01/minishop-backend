package io.userpermission.form;

import io.userpermission.validator.UniqueAuthGroupName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthGroupRegistrationForm extends AuthGroupEditForm {

    @UniqueAuthGroupName(message = "error.duplicate.name")
    public String getName() {
        return super.getName();
    }
}
