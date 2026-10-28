package io.internaluser.view;

import io.internaluser.entity.InternalUser;
import io.user.view.UserView;

public class InternalUserView extends UserView<InternalUser> {

    public InternalUserView(InternalUser entity) {
        super(entity);
    }

    public String getOrgBranchName() {
        return entity.getOrgBranch() == null
                ? null
                : entity.getOrgBranch().getName();
    }
    public String getOrgDepartmentName(){
        return entity.getOrgDepartment() == null
                ? null
                : entity.getOrgDepartment().getName();
    }

    public String getErpSalesPersonCode() {
        return entity.getErpSalesPersonCode() == null
                ? null
                :entity.getErpSalesPersonCode();
    }
}