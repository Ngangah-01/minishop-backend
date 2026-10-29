package io.orgbranch.view;

import io.lib.view.BaseView;
import io.orgbranch.entity.OrgBranch;

public class OrgBranchView extends BaseView<OrgBranch> {
    public OrgBranchView(OrgBranch entity) {
        super(entity);
    }

    public String getName(){
        return entity.getName();
    }
}
