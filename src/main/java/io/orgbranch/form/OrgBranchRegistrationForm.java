package io.orgbranch.form;

import io.orgbranch.validator.UniqueOrgBranchName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrgBranchRegistrationForm extends OrgBranchEditForm{
    @UniqueOrgBranchName(message = "error.duplicate.name")
    public String getName() {
        return super.getName();
    }
}
