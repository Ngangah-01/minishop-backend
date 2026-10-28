package io.userpermission.view;

import io.lib.view.BaseView;
import io.userpermission.model.UserPermission;

public class UserPermissionView extends BaseView<UserPermission> {

    public UserPermissionView(UserPermission entity) {
        super(entity);
    }

    public String getDescription(){
        return entity.getDescription();
    }

    public String getPermissionName(){
        return entity.getPermissionName();
    }
}
