package io.internaluser.service;

import io.internaluser.entity.InternalUser;
import io.internaluser.entity.InternalUserAuthGroup;
import io.internaluser.repository.InternalUserAuthGroupRepository;
import io.lib.service.BaseJpaRepoReadService;
import io.user.form.FetchUserForm;
import io.userpermission.model.AuthGroup;
import io.userpermission.service.AuthGroupReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternalUserAuthGroupReadService extends BaseJpaRepoReadService<
        InternalUserAuthGroup, InternalUserAuthGroupRepository> {
    private AuthGroupReadService authGroupReadService;

    List<InternalUserAuthGroup> findByInternalUser(InternalUser internalUser){
        return repository.findByInternalUser(internalUser);
    }

    public List<AuthGroup> findAuthGroupsByInternalUser(InternalUser internalUser){
        var internalUserGroups = findByInternalUser(internalUser);
        return internalUserGroups.stream().map(InternalUserAuthGroup::getAuthGroup).toList();
    }

    public Page<InternalUserAuthGroup> listByAuthGroup(FetchUserForm form, String authGroupId){
        var authGroup = authGroupReadService.findByEntityId(authGroupId);
        var pageable = repository.defaultPageable(form);
        return repository.findByAuthGroup(authGroup, pageable);
    }

    @Autowired
    public void setAuthGroupReadService(AuthGroupReadService service){
        this.authGroupReadService = service;
    }
}
