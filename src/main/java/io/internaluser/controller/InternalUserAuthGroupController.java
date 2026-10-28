package io.internaluser.controller;

import io.internaluser.entity.InternalUser;
import io.internaluser.form.UpdateUserAuthGroupForm;
import io.internaluser.service.InternalUserAuthGroupReadService;
import io.internaluser.service.InternalUserEditService;
import io.internaluser.view.InternalUserAuthView;
import io.lib.service.Message;
import io.lib.view.EntityApiResponse;
import io.userpermission.view.AuthGroupView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@RestController
@RequestMapping(INTERNAL_USER_BASE_URL +"/internal-users/auth-groups")
public class InternalUserAuthGroupController {
    private InternalUserEditService internalUserEditService;
    private InternalUserAuthGroupReadService internalUserAuthGroupReadService;

    @PreAuthorize("hasAuthority('EDIT_INTERNAL_USER_AUTH_GROUP')")
    @PostMapping("edit/{internalUserId}")
    public EntityApiResponse<InternalUserAuthView> updatePermissions(
            @PathVariable String internalUserId,
            @RequestBody @Valid UpdateUserAuthGroupForm form,
            Authentication auth,
            Locale locale){
        form.setSessionUserId(auth.getName());
        InternalUser internalUser = internalUserEditService.updateInternalUserAuthGroups(form, internalUserId);

        return new EntityApiResponse<>(
            Message.get("user.permissions.update.success", locale),
            new InternalUserAuthView(internalUser)
        );
    }

    @GetMapping("list/{internalUserId}")
    public EntityApiResponse<List<AuthGroupView>> list(@PathVariable String internalUserId){
        var user = internalUserEditService.findByEntityId(internalUserId);
        var authGroups = internalUserAuthGroupReadService.findAuthGroupsByInternalUser(user);
        var views = new HashSet<>(authGroups).stream().map(AuthGroupView::new).toList();
        return new EntityApiResponse<>(views);
    }

    @Autowired
    public void setInternalUserManagementService(InternalUserEditService internalUserEditService) {
        this.internalUserEditService = internalUserEditService;
    }

    @Autowired
    public void setInternalUserAuthGroupReadService(InternalUserAuthGroupReadService service) {
        this.internalUserAuthGroupReadService = service;
    }
}
