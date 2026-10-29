package io.internaluser.form;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class InternalUserRegistrationForm extends InternalUserEditForm {
    private Set<String> authGroupIds;
}
