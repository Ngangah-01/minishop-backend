package io.orgbranch.service;

import io.activitylog.form.CreateActivityLogForm;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.service.BaseJpaRepoEditService;
import io.orgbranch.entity.OrgBranch;
import io.orgbranch.form.OrgBranchEditForm;
import io.orgbranch.form.OrgBranchRegistrationForm;
import io.orgbranch.repository.OrgBranchRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class OrgBranchEditService extends BaseJpaRepoEditService<OrgBranch, OrgBranchRepository> {

    public OrgBranch registerOrgBranch(OrgBranchRegistrationForm form){
        var orgBranch = new OrgBranch();
        orgBranch.setName(form.getName());
        orgBranch.setCreatedByEntityId(form.getSessionUserId());
        save(orgBranch, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(orgBranch.getEntityId());
        activityLogForm.setAction("Branch creation");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return orgBranch;
    }

    public OrgBranch updateOrgBranch(OrgBranchEditForm form, String orgBranchId){
        Specification<OrgBranch> spec = repository.notDeleted()
                .and(repository.nameIs(form.getName()))
                .and(repository.entityIdNot(orgBranchId));

        boolean exists = repository.exists(spec);

        if(exists){
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.duplicate.name");
        }

        var orgBranch = findByEntityId(orgBranchId);
        orgBranch.setName(form.getName());
        save(orgBranch, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(orgBranch.getEntityId());
        activityLogForm.setAction("Branch update.");
        activityLogForm.setRemarks(String.format("Name: %s", orgBranch.getName()));
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return orgBranch;
    }
}
