package io.userpermission.service;

import io.lib.service.BaseJpaRepoEditService;
import io.userpermission.model.AuthGroup;
import io.userpermission.model.AuthGroupPermission;
import io.userpermission.model.UserPermission;
import io.userpermission.repository.AuthGroupPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthGroupPermissionEditService extends BaseJpaRepoEditService<AuthGroupPermission, AuthGroupPermissionRepository> {
    private UserPermissionReadService userPermissionService;

    void registerAuthGroupPermissions(AuthGroup authGroup, List<String> permissionIds, String createdById){
        List<UserPermission> perms = userPermissionService.findByIds(permissionIds);
        List<AuthGroupPermission> permGroups = new ArrayList<>();

        for(UserPermission perm: perms){
            AuthGroupPermission authGrpPerm = new AuthGroupPermission();
            authGrpPerm.setAuthGroup(authGroup);
            authGrpPerm.setUserPermission(perm);
            authGrpPerm.setCreatedByEntityId(createdById);
            permGroups.add(authGrpPerm);
        }
        save(permGroups, createdById);
    }

    void deleteAuthGroupGroupPermissionByAuthGroup(AuthGroup authGroup){
        Specification<AuthGroupPermission> spec = repository.notDeleted()
                .and(repository.authGroupIdIn(List.of(authGroup)));
        repository.deleteAll(repository.findAll(spec));
    }

    @Autowired
    public void setPermissionService(UserPermissionReadService userPermissionService) {
        this.userPermissionService = userPermissionService;
    }
}
