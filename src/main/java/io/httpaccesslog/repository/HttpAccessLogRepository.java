package io.httpaccesslog.repository;

import io.httpaccesslog.entity.HttpAccessLog;
import io.lib.repository.BaseMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HttpAccessLogRepository extends BaseMongoRepository<HttpAccessLog> {
}
