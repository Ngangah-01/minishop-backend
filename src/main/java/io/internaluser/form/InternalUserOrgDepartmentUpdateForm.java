package io.internaluser.form;

import io.lib.form.SessionUserIdForm;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InternalUserOrgDepartmentUpdateForm extends SessionUserIdForm {
    private String orgDepartmentId;
}
