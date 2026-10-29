package io.internaluser.service;

import io.activitylog.form.CreateActivityLogForm;
import io.internaluser.entity.InternalUser;
import io.internaluser.form.*;
import io.internaluser.repository.InternalUserRepository;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.form.RemarksForm;
import io.orgbranch.service.OrgBranchReadService;
import io.orgdepartment.entity.OrgDepartment;
import io.orgdepartment.service.OrgDepartmentReadService;
import io.user.entity.UserStatus;
import io.user.form.UserEditForm;
import io.user.service.BaseUserEditService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InternalUserEditService extends BaseUserEditService<InternalUser, InternalUserRepository> {
    private InternalUserAuthGroupEditService internalUserAuthGroupEditService;
    private OrgBranchReadService orgBranchReadService;
    private OrgDepartmentReadService orgDepartmentReadService;

    public InternalUser register(InternalUserRegistrationForm form) {
        var user = super.register(form);

        var branchUpdateForm = new InternalUserOrgBranchUpdateForm();
        branchUpdateForm.setOrgBranchId(form.getOrgBranchId());
        branchUpdateForm.setSessionUserId(form.getSessionUserId());
        user = setUserOrgBranch(user.getEntityId(), branchUpdateForm);


        var departmentUpdateForm = new InternalUserOrgDepartmentUpdateForm();
        departmentUpdateForm.setOrgDepartmentId(form.getOrgDepartmentId());
        departmentUpdateForm.setSessionUserId(form.getSessionUserId());
        user = setUserOrgDepartment(departmentUpdateForm, user.getEntityId());

        internalUserAuthGroupEditService.registerUserToAuthGroup(user, form.getAuthGroupIds(), form.getSessionUserId());
        return user;
    }

    public InternalUser suspend(String userId, RemarksForm form) {
        if (form.getSessionUserId().equals(userId)) {
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.operation.not.allowed");
        }
        return super.suspend(userId, form);
    }

    public InternalUser activate(String userId, RemarksForm form) {
        if (form.getSessionUserId().equals(userId)) {
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.operation.not.allowed");
        }
        return super.activate(userId, form);
    }

    public InternalUser edit(String userId, UserEditForm form) {
        if (userId.equals(form.getSessionUserId())) {
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.operation.not.allowed");
        }

        return super.edit(userId, form);
    }

    public InternalUser updateInternalUserAuthGroups(UpdateUserAuthGroupForm form, String internalUserId) {
        InternalUser userToUpdate = findByEntityId(internalUserId);

        if (internalUserId.equals(form.getSessionUserId())) {
            throw new CommonRuntimeException(ExceptionType.FORBIDDEN, "error.operation.not.allowed");
        }

        if (userToUpdate.getUserStatus() != UserStatus.ACTIVE) {
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "user.status.active.required");
        }

        internalUserAuthGroupEditService.registerUserToAuthGroup(userToUpdate, form.getAuthGroupIds(), form.getSessionUserId());

        return userToUpdate;
    }
    public InternalUser setUserOrgDepartment(InternalUserOrgDepartmentUpdateForm form, String internalUserId) {
        var userToUpdate = findByEntityId(internalUserId);

        if (StringUtils.isNotBlank(form.getOrgDepartmentId())) {
            OrgDepartment department = orgDepartmentReadService.findByEntityId(form.getOrgDepartmentId());
            userToUpdate.setOrgDepartment(department);
        } else {
            userToUpdate.setOrgDepartment(null);
        }
        userToUpdate = save(userToUpdate, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(userToUpdate.getEntityId());
        activityLogForm.setAction("Department update.");
        String remarks = userToUpdate.getOrgDepartment() != null
                ? String.format("Department set to: %s", userToUpdate.getOrgDepartment().getName())
                : "Department set to null.";
        activityLogForm.setRemarks(remarks);

        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return userToUpdate;
    }

    public InternalUser setUserOrgBranch(String internalUserId, InternalUserOrgBranchUpdateForm form) {
        var user = findByEntityId(internalUserId);

        if (StringUtils.isNotBlank(form.getOrgBranchId())) {
            var orgBranch = orgBranchReadService.findByEntityId(form.getOrgBranchId());
            user.setOrgBranch(orgBranch);
        } else {
            user.setOrgBranch(null);
        }

        user = save(user, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(user.getEntityId());
        activityLogForm.setAction("Branch update.");
        String remarks = user.getOrgBranch() != null
                ? String.format("Branch set to: %s", user.getOrgBranch().getName())
                : "Branch set to null.";
        activityLogForm.setRemarks(remarks);

        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return user;
    }

    public InternalUser setSalesPersonCode(String internalUserId, SalesPersonCodeUpdateForm form){
        if (internalUserId.equals(form.getSessionUserId())) {
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.operation.not.allowed");
        }

        var user = findByEntityId(internalUserId);

        if (StringUtils.isNotBlank(form.getErpSalesPersonCode())) {
            user.setErpSalesPersonCode(form.getErpSalesPersonCode());
        } else {
            user.setErpSalesPersonCode(null);
        }

        user = save(user, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(user.getEntityId());
        activityLogForm.setAction("ERP username update.");
        String remarks = String.format(
                "Sales person's code set to: %s",
                user.getErpSalesPersonCode()
        );
        activityLogForm.setRemarks(remarks);
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return user;
    }

    @Override
    protected InternalUser getNewUser() {
        return new InternalUser();
    }

    @Autowired
    public void setInternalUserAuthGroupEditService(InternalUserAuthGroupEditService service) {
        this.internalUserAuthGroupEditService = service;
    }

    @Autowired
    public void setOrgBranchReadService(OrgBranchReadService service) {
        this.orgBranchReadService = service;
    }

    @Autowired
    public void setOrgDepartmentReadService(OrgDepartmentReadService service) {
        this.orgDepartmentReadService = service;
    }
}