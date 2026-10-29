package io.internaluser.service;

import io.internaluser.entity.InternalUser;
import io.internaluser.form.InternalUserFetchForm;
import io.internaluser.repository.InternalUserRepository;
import io.internaluser.view.InternalUserAuthView;
import io.orgbranch.service.OrgBranchReadService;
import io.user.service.BaseUserReadService;
import io.userpermission.model.AuthGroup;
import io.userpermission.model.UserPermission;
import io.userpermission.service.AuthGroupPermissionReadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternalUserReadService extends BaseUserReadService<InternalUser, InternalUserRepository> {
    private AuthGroupPermissionReadService authGroupPermissionReadService;
    private InternalUserAuthGroupReadService internalUserAuthGroupReadService;
    private OrgBranchReadService orgBranchReadService;

    public InternalUserAuthView getInternalUserAuthView(InternalUser user){
        InternalUserAuthView view = new InternalUserAuthView(user);
        List<AuthGroup> groups = internalUserAuthGroupReadService.findAuthGroupsByInternalUser(user);
        List<UserPermission> perms = authGroupPermissionReadService.findPermissionsByAuthGroups(groups);
        view.setUserPermissions(perms);
        view.setAuthGroups(groups);
        return view;
    }

    public Page<InternalUser> listUsers(InternalUserFetchForm form) {
        var spec = repository.notDeleted();

        if (StringUtils.isNotBlank(form.getQuery())) {
            spec = spec.and(
                repository.nameLike(form.getQuery())
                    .or(repository.emailLike(form.getQuery()))
                    .or(repository.phoneNumberLike(form.getQuery()))
            );
        }

        if (form.getUserStatus() != null) {
            spec = spec.and(repository.userStatusIs(form.getUserStatus()));
        }

        if(StringUtils.isNotBlank(form.getOrgBranchId())) {
            var orgBranch = orgBranchReadService.findByEntityId(form.getOrgBranchId());
            spec = spec.and(repository.orgBranchIs(orgBranch));
        }

        if(StringUtils.isNotBlank(form.getSessionUserId())){
            var orgBranch = findByEntityId(form.getSessionUserId()).getOrgBranch();
            if(orgBranch != null) {
                spec = spec.and(repository.orgBranchIs(orgBranch));
            }
        }

        if(StringUtils.isNotBlank(form.getDepartmentInfo())) {
            spec = spec.and(repository.departmentNameLike(form.getDepartmentInfo()));
        }

        if(StringUtils.isNotBlank(form.getOrgDepartmentId())) {
            spec = spec.and(repository.orgDepartmentIdIs(form.getOrgDepartmentId()));
        }

        return repository.findAll(spec, repository.defaultPageable(form));
    }

    public InternalUser findBySalesPersonCode(String salesPersonCode){
        var spec = repository.notDeleted()
                .and(repository.salesPersonCodeIs(salesPersonCode));
        return repository.findOne(spec).orElse(null);
    }

    @Autowired
    public void setInternalUserAuthGroupReadService(AuthGroupPermissionReadService service){
        this.authGroupPermissionReadService = service;
    }

    @Autowired
    public void setInternalUserAuthGroupReadService(InternalUserAuthGroupReadService service){
        this.internalUserAuthGroupReadService = service;
    }

    @Autowired
    public void setOrgBranchReadService(OrgBranchReadService service){
        this.orgBranchReadService = service;
    }
}