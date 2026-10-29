package io.userpermission.controller;

import io.lib.form.RemarksForm;
import io.lib.service.Message;
import io.lib.view.ApiResponse;
import io.lib.view.EntityApiResponse;
import io.lib.view.PagedEntityApiResponse;
import io.userpermission.form.AuthGroupEditForm;
import io.userpermission.form.AuthGroupRegistrationForm;
import io.userpermission.form.FetchAuthGroupForm;
import io.userpermission.form.FetchUserPermissionForm;
import io.userpermission.model.AuthGroup;
import io.userpermission.model.AuthGroupPermission;
import io.userpermission.service.AuthGroupEditService;
import io.userpermission.service.AuthGroupPermissionReadService;
import io.userpermission.service.AuthGroupReadService;
import io.userpermission.validator.AuthGroupIdExists;
import io.userpermission.validator.AuthGroupIsEditable;
import io.userpermission.view.AuthGroupView;
import io.userpermission.view.UserPermissionView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/auth-groups")
@Validated
public class AuthGroupController {
    private AuthGroupEditService authGroupEditService;
    private AuthGroupReadService authGroupReadService;
    private AuthGroupPermissionReadService authGroupPermissionReadService;

    @PreAuthorize("hasAuthority('VIEW_AUTH_GROUP')")
    @GetMapping("list")
    public PagedEntityApiResponse<AuthGroupView> listCustomers(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize){

        var form = new FetchAuthGroupForm();
        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);

        var page = authGroupReadService.listGroups(form);

        var views = page.stream()
                .map(AuthGroupView::new)
                .toList();

        return new PagedEntityApiResponse<>(page, views);
    }

    @PreAuthorize("hasAuthority('REGISTER_AUTH_GROUP')")
    @PostMapping("register")
    public EntityApiResponse<AuthGroupView> register(
            @RequestBody @Valid AuthGroupRegistrationForm form,
            Authentication auth,
            Locale locale){
        form.setSessionUserId(auth.getName());
        AuthGroup authGroup = authGroupEditService.create(form);
        return new EntityApiResponse<>(
            Message.get("auth.group.creation.success", locale),
            new AuthGroupView(authGroup)
        );
    }

    @PreAuthorize("hasAuthority('DELETE_AUTH_GROUP')")
    @PostMapping("delete/{authGroupId}")
    public ApiResponse delete(
            @PathVariable
            @AuthGroupIdExists(message = "error.entity.not.found")
            @AuthGroupIsEditable(message = "error.operation.not.allowed")
            String authGroupId,
            @RequestBody @Valid RemarksForm form,
            Authentication auth, Locale locale){
        form.setSessionUserId(auth.getName());
        authGroupEditService.delete(form, authGroupId);
        return new ApiResponse(
            Message.get("auth.group.deletion.success", locale)
        );
    }

    @PreAuthorize("hasAuthority('EDIT_AUTH_GROUP')")
    @PostMapping("edit/{authGroupId}")
    public EntityApiResponse<AuthGroupView> edit(
        @PathVariable
        @AuthGroupIdExists(message = "error.entity.not.found")
        @AuthGroupIsEditable(message = "error.operation.not.allowed")
        String authGroupId,
        @RequestBody
        @Valid AuthGroupEditForm form,
        Authentication auth, Locale locale){
        form.setSessionUserId(auth.getName());
        var authGroup = authGroupEditService.edit(form, authGroupId);
        return new EntityApiResponse<>(
            Message.get("auth.group.edit.success", locale),
            new AuthGroupView(authGroup)
        );
    }

    @PreAuthorize("hasAuthority('VIEW_AUTH_GROUP')")
    @GetMapping(value = "permissions/{authGroupId}")
    public PagedEntityApiResponse<UserPermissionView> listPermissions(
            @PathVariable
            @AuthGroupIdExists(message = "error.entity.not.found")
            String authGroupId,
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize){

        var form = new FetchUserPermissionForm();
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setAuthGroupId(authGroupId);
        var page = authGroupPermissionReadService.listAuthGroupPermissions(form);

        var views = page.stream()
                .map(AuthGroupPermission::getUserPermission)
                .map(UserPermissionView::new)
                .toList();

        return new PagedEntityApiResponse<>(page, views);
    }

    @Autowired
    public void setAuthGroupEditService(AuthGroupEditService service) {
        this.authGroupEditService = service;
    }

    @Autowired
    public void setAuthGroupReadService(AuthGroupReadService service) {
        this.authGroupReadService = service;
    }

    @Autowired
    public void setAuthGroupPermissionReadService(AuthGroupPermissionReadService service) {
        this.authGroupPermissionReadService = service;
    }

}
