package io.activitylog.entity;

import io.lib.entity.BaseMongoEntity;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityLog  extends BaseMongoEntity {

    private String owningEntityId;
    private String actorUsername;

    @Column(length = 256)
    private String action;

    @Column(length = 1024)
    private String remarks;
}
