package io.internaluser.service;

import io.activitylog.form.CreateActivityLogForm;
import io.internaluser.entity.InternalUser;
import io.internaluser.entity.InternalUserAuthGroup;
import io.internaluser.repository.InternalUserAuthGroupRepository;
import io.lib.service.BaseJpaRepoEditService;
import io.userpermission.model.AuthGroup;
import io.userpermission.service.AuthGroupReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InternalUserAuthGroupEditService extends BaseJpaRepoEditService<InternalUserAuthGroup, InternalUserAuthGroupRepository> {
    private AuthGroupReadService authGroupReadService;


    void registerUserToAuthGroup(InternalUser internalUser, Set<String> authGroupIds, String createdById) {
        var previousAuthGroups = findByInternalUser(internalUser);
        repository.deleteAll(previousAuthGroups);

        List<AuthGroup> authGroups = new ArrayList<>();
        if (authGroupIds != null && !authGroupIds.isEmpty()) {
            authGroups = addUserToAuthGroup(internalUser, authGroupIds, createdById);
        }

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(internalUser.getEntityId());
        activityLogForm.setAction("User auth group update");
        String remarks = authGroups.isEmpty()
                ? "Removed from all auth groups"
                : String.format("Added to auth groups:\n%s", authGroups.stream().map(AuthGroup::getName).collect(Collectors.joining(",")));
        activityLogForm.setRemarks(remarks);
        activityLogForm.setSessionUserId(createdById);
        activityLogQueuingService.enqueueActivityLog(activityLogForm);
    }

    private List<InternalUserAuthGroup> findByInternalUser(InternalUser internalUser){
        return repository.findByInternalUser(internalUser);
    }

    private List<AuthGroup> addUserToAuthGroup(InternalUser internalUser, Set<String> authGroupIds, String createdById) {
        var authGroups = authGroupReadService.findByIds(new ArrayList<>(authGroupIds));
        List<InternalUserAuthGroup> userAuthGroups = new ArrayList<>();
        for (AuthGroup authGroup : authGroups) {
            InternalUserAuthGroup userAuthGroup = new InternalUserAuthGroup();
            userAuthGroup.setInternalUser(internalUser);
            userAuthGroup.setAuthGroup(authGroup);
            userAuthGroup.setCreatedByEntityId(createdById);
            userAuthGroups.add(userAuthGroup);
        }
        save(userAuthGroups, createdById);
        return authGroups;
    }

    @Autowired
    public void setAuthGroupReadService(AuthGroupReadService service) {
        this.authGroupReadService = service;
    }

}
