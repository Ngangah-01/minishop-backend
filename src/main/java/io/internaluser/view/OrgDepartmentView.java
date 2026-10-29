package io.internaluser.view;

import io.lib.view.BaseView;
import io.orgdepartment.entity.OrgDepartment;

public class OrgDepartmentView extends BaseView<OrgDepartment> {
    public OrgDepartmentView(OrgDepartment entity) {
        super(entity);
    }

    public String getName(){
        return entity.getName();
    }
}
