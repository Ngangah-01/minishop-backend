package io.internaluser.service;


import io.internaluser.entity.InternalUserAuthGroup;
import io.internaluser.repository.InternalUserAuthGroupRepository;
import io.lib.service.BaseJpaRepoEditService;
import io.userpermission.model.AuthGroup;
import io.userpermission.service.AuthGroupDeletionListener;
import org.springframework.stereotype.Service;

@Service
public class InternalUserAuthGroupDeletionService
        extends BaseJpaRepoEditService<InternalUserAuthGroup, InternalUserAuthGroupRepository>
        implements AuthGroupDeletionListener {
    @Override
    public void onAuthGroupDeletion(AuthGroup authGroup) {
        var internalUserAuthGroups = repository.findByAuthGroup(authGroup);
        repository.deleteAll(internalUserAuthGroups);
    }
}
