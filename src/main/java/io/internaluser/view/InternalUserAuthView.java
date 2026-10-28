package io.internaluser.view;

import io.internaluser.entity.InternalUser;
import io.userpermission.model.AuthGroup;
import io.userpermission.model.UserPermission;
import io.userpermission.view.AuthGroupView;
import io.userpermission.view.UserPermissionView;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class InternalUserAuthView extends InternalUserView {

    private List<UserPermission> userPermissions;

    private List<AuthGroup> authGroups;

    public InternalUserAuthView(InternalUser entity) {
        super(entity);
    }

    public List<AuthGroupView> getAuthGroups() {
        List<AuthGroupView> views = new ArrayList<>();
        if(this.authGroups != null){
            views = authGroups.stream().map(AuthGroupView::new).toList();
        }
        return views;
    }

    public List<UserPermissionView> getUserPermissions(){
        List<UserPermissionView> views = new ArrayList<>();
        if(this.userPermissions != null){
            var perms = new ArrayList<>(this.userPermissions);
            perms.sort(Comparator.comparing(UserPermission::getPermissionName));
            views = perms.stream().map(UserPermissionView::new).toList();
        }
        return views;
    }
}
