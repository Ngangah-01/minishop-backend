package io.internaluser.form;

import io.user.form.FetchUserForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalUserFetchForm extends FetchUserForm {
    private String orgBranchId;
    private String departmentInfo;
    private String orgDepartmentId;
}

