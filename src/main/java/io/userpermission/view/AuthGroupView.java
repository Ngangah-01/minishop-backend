package io.userpermission.view;



import io.lib.view.BaseView;
import io.userpermission.model.AuthGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AuthGroupView extends BaseView<AuthGroup> {
    public AuthGroupView(AuthGroup entity) {
        super(entity);
    }

    public String getName(){
        return entity.getName();
    }

    public String getDescription(){
        return entity.getDescription();
    }

    public boolean getIsEditable(){
        return entity.isEditable();
    }

    public Integer getPermissionsCount(){
        int count = 0;
        if(entity.getAuthGroupPermissions() != null){
            count = entity.getAuthGroupPermissions().size();
        }
        return count;
    }

    public List<UserPermissionView> getUserPermissions(){
        List<UserPermissionView> permViews = List.of();
        if(entity.getAuthGroupPermissions() != null){
            var grpPerms = new ArrayList<>(entity.getAuthGroupPermissions());
            grpPerms.sort(Comparator.comparing(o -> o.getUserPermission().getPermissionName()));

            permViews = grpPerms.stream()
                    .map(grpPerm -> new UserPermissionView(grpPerm.getUserPermission()))
                    .toList();
        }
        return permViews;
    }
}
