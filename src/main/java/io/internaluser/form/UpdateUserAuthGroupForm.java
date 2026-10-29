package io.internaluser.form;

import io.lib.form.SessionUserIdForm;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdateUserAuthGroupForm extends SessionUserIdForm {
    private Set<String> authGroupIds;
}
