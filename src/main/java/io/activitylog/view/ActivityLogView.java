package io.activitylog.view;


import io.activitylog.entity.ActivityLog;

import java.time.LocalDateTime;


public class ActivityLogView  {
    private final ActivityLog entity;

    public ActivityLogView(ActivityLog entity) {
       this.entity = entity;
    }

    public String getId(){
        return entity.getId();
    }

    public LocalDateTime getCreatedAt(){
        return entity.getCreatedAt();
    }

    public String getAction(){
        return entity.getAction();
    }

    public String getActorUsername(){
        return entity.getActorUsername();
    }

    public String getRemarks(){
        return entity.getRemarks();
    }
}
