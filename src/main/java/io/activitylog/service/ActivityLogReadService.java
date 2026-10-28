package io.activitylog.service;

import io.activitylog.entity.ActivityLog;
import io.activitylog.form.FetchActivityLogForm;
import io.activitylog.repository.ActivityLogRepository;
import io.lib.service.BaseMongoRepoService;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;


@Service
public class ActivityLogReadService extends BaseMongoRepoService<ActivityLog, ActivityLogRepository> {


    public Page<ActivityLog> listActivityLogs(FetchActivityLogForm form){
        var pageable = repository.defaultPageable(form);
        var query = new Query().with(pageable);
        query.addCriteria(Criteria.where("deletedAt").exists(false));
        query.addCriteria(Criteria.where("owningEntityId").is(form.getOwningEntityId()));

       var activityLogs = mongoTemplate.find(query, ActivityLog.class);

        return PageableExecutionUtils.getPage(
                activityLogs,
                pageable,
                () -> mongoTemplate.count(query.limit(-1).skip(-1), ActivityLog.class)
        );
    }

}
