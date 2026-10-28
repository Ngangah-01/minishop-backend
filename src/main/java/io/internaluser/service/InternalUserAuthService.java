package io.internaluser.service;

import io.internaluser.entity.InternalUser;
import io.internaluser.repository.InternalUserRepository;
import io.user.entity.UserStatus;
import io.user.entity.UserType;
import io.user.service.UserAuthService;
import io.userpermission.model.UserPermission;
import io.userpermission.service.AuthGroupPermissionReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class InternalUserAuthService extends UserAuthService<InternalUser, InternalUserRepository> {
    private InternalUserAuthGroupReadService internalUserAuthGroupReadService;
    private AuthGroupPermissionReadService authGroupPermissionReadService;

    @Override
    public List<String> getUserPermissions(InternalUser user) {
        List<String> permNames = List.of();

        if (user.getUserStatus() == UserStatus.ACTIVE) {
            var authGroups = internalUserAuthGroupReadService.findAuthGroupsByInternalUser(user);
            var perms = authGroupPermissionReadService.findPermissionsByAuthGroups(authGroups);
            permNames = perms.stream().map(UserPermission::getPermissionName).toList();
        }

        return permNames;
    }

    @Override
    public UserType getUserType() {
        return UserType.INTERNAL_USER;
    }

    @Autowired
    public void setInternalUserAuthGroupReadService(InternalUserAuthGroupReadService service){
        this.internalUserAuthGroupReadService = service;
    }

    @Autowired
    public void setAuthGroupPermissionReadService(AuthGroupPermissionReadService service) {
        this.authGroupPermissionReadService = service;
    }
}
