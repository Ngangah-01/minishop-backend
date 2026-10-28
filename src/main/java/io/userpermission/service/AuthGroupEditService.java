package io.userpermission.service;

import io.activitylog.form.CreateActivityLogForm;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.form.RemarksForm;
import io.lib.service.BaseJpaRepoEditService;
import io.userpermission.form.AuthGroupEditForm;
import io.userpermission.form.AuthGroupRegistrationForm;
import io.userpermission.model.AuthGroup;
import io.userpermission.repository.AuthGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AuthGroupEditService extends BaseJpaRepoEditService<AuthGroup, AuthGroupRepository> {
    private AuthGroupPermissionEditService authGroupPermissionEditService;

    private List<AuthGroupDeletionListener> authGroupDeletionListeners;

    public AuthGroup create(AuthGroupRegistrationForm form){
        AuthGroup authGroup = new AuthGroup();
        authGroup.setName(form.getName());
        authGroup.setDescription(form.getDescription());
        authGroup.setEditable(true);
        authGroup.setCreatedByEntityId(form.getSessionUserId());

        authGroup = save(authGroup, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(authGroup.getEntityId());
        activityLogForm.setAction("Auth group creation");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        authGroupPermissionEditService.registerAuthGroupPermissions(
            authGroup,
            List.copyOf(form.getPermissionIds()),
            form.getSessionUserId()
        );
        return authGroup;
    }

    public void delete(RemarksForm form, String authGroupId){
        var authGroup = findByEntityId(authGroupId);
        delete(authGroup, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(authGroup.getEntityId());
        activityLogForm.setAction("Auth group deletion");
        activityLogForm.setRemarks(form.getRemarks());
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        authGroupPermissionEditService.deleteAuthGroupGroupPermissionByAuthGroup(authGroup);
        authGroupDeletionListeners.forEach(listener -> listener.onAuthGroupDeletion(authGroup));
    }

    public AuthGroup edit(AuthGroupEditForm form, String authGroupId){
        var spec = repository.notDeleted()
            .and(repository.nameIs(form.getName()))
            .and(repository.entityIdNot(authGroupId));

        boolean exists = repository.exists(spec);

        if(exists){
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.duplicate.name");
        }

        AuthGroup authGroup = findByEntityId(authGroupId);
        authGroup.setName(form.getName());
        authGroup.setDescription(form.getDescription());
        authGroup = save(authGroup, form.getSessionUserId());

        CreateActivityLogForm activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(authGroup.getEntityId());
        activityLogForm.setAction("Auth group edit");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        authGroupPermissionEditService.deleteAuthGroupGroupPermissionByAuthGroup(authGroup);
        authGroupPermissionEditService.registerAuthGroupPermissions(
            authGroup,
            List.copyOf(form.getPermissionIds()),
            form.getSessionUserId()
        );
        return authGroup;
    }

    @Autowired
    public void setAuthGroupPermissionService(AuthGroupPermissionEditService authGroupPermissionEditService) {
        this.authGroupPermissionEditService = authGroupPermissionEditService;
    }

    @Autowired
    public void setAuthGroupDeletionListeners(List<AuthGroupDeletionListener> authGroupDeletionListeners) {
        this.authGroupDeletionListeners = authGroupDeletionListeners;
    }
}
