package io.userpermission.service;

import io.lib.service.BaseJpaRepoReadService;
import io.userpermission.form.FetchUserPermissionForm;
import io.userpermission.model.AuthGroup;
import io.userpermission.model.AuthGroupPermission;
import io.userpermission.model.UserPermission;
import io.userpermission.repository.AuthGroupPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthGroupPermissionReadService extends BaseJpaRepoReadService<AuthGroupPermission, AuthGroupPermissionRepository> {
    private AuthGroupReadService authGroupReadService;

    public Page<AuthGroupPermission> listAuthGroupPermissions(FetchUserPermissionForm form){
        var authGroup = authGroupReadService.findByEntityId(form.getAuthGroupId());
        var spec = repository.notDeleted()
                .and(repository.authGroupIdIn(List.of(authGroup)));
        return repository.findAll(spec, repository.defaultPageable(form));
    }

    public List<UserPermission> findPermissionsByAuthGroups(List<AuthGroup> authGroups){
        var spec = repository.notDeleted()
                .and(repository.authGroupIdIn(authGroups));
        var authGroupPermissions =  repository.findAll(spec);
        return authGroupPermissions.stream().map(AuthGroupPermission::getUserPermission).toList();
    }

    @Autowired
    public void setAuthGroupReadService(AuthGroupReadService service) {
        this.authGroupReadService = service;
    }
}
