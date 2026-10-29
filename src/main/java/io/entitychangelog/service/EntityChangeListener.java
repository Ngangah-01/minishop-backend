package io.entitychangelog.service;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PreUpdate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class EntityChangeListener implements ApplicationContextAware {
    private static ApplicationContext context;

    @PostPersist
    public void postPersist(Object entity) {
        if (context != null) {
            EntityChangeLogService service = context.getBean(EntityChangeLogService.class);
            service.enqueueEntityChange(entity);
        }
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        if (context != null) {
            EntityChangeLogService service = context.getBean(EntityChangeLogService.class);
            service.enqueueEntityChange(entity);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        context = ctx;
    }
}
