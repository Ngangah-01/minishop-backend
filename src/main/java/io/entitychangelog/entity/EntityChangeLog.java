package io.entitychangelog.entity;

import io.lib.entity.BaseMongoEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class EntityChangeLog extends BaseMongoEntity {
    private String entityId;
    private Object object;
}
