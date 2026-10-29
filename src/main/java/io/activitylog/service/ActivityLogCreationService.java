package io.activitylog.service;

import io.activitylog.entity.ActivityLog;
import io.activitylog.form.CreateActivityLogForm;
import io.activitylog.repository.ActivityLogRepository;
import io.internaluser.service.InternalUserReadService;
import io.lib.service.BaseMongoRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
class ActivityLogCreationService extends BaseMongoRepoService<ActivityLog, ActivityLogRepository> {
    private InternalUserReadService internalUserReadService;


    void saveActivityLog(CreateActivityLogForm form) {
        var activityLog = new ActivityLog();
        activityLog.setAction(form.getAction());
        activityLog.setActorUsername(resolveActorUsername(form.getSessionUserId()));
        activityLog.setCreatedByEntityId(form.getSessionUserId());
        activityLog.setRemarks(form.getRemarks());
        activityLog.setOwningEntityId(form.getOwningEntityId());

        save(activityLog, form.getSessionUserId());
    }

    private String resolveActorUsername(String actorEntityId){
        boolean isInternalUser = internalUserReadService.existsByEntityId(actorEntityId);

        if(isInternalUser){
            var internalUser = internalUserReadService.findByEntityId(actorEntityId);
            return internalUser.getUsername();
        }

        return null;
    }

    @Autowired
    public void setInternalUserReadService(InternalUserReadService service) {
        this.internalUserReadService = service;
    }
}
