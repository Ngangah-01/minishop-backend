package io.entitychangelog.repository;


import io.entitychangelog.entity.EntityChangeLog;
import io.lib.repository.BaseMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityChangeLogRepository extends BaseMongoRepository<EntityChangeLog> {
}
