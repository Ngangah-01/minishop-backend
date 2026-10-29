package io.activitylog.repository;

import io.activitylog.entity.ActivityLog;
import io.lib.repository.BaseMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLogRepository extends BaseMongoRepository<ActivityLog> {

}
