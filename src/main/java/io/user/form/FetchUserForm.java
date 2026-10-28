package io.user.form;

import io.lib.form.BaseDatedFetchForm;
import io.user.entity.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FetchUserForm extends BaseDatedFetchForm {
    private UserStatus userStatus;
}
