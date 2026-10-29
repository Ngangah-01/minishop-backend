package io.lib.service;

import io.activitylog.service.ActivityLogQueuingService;
import io.lib.entity.BaseJpaEntity;
import io.lib.repository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public abstract class BaseJpaRepoEditService<T extends BaseJpaEntity, R extends BaseJpaRepository<T>> extends BaseJpaRepoReadService<T, R> {

    protected ActivityLogQueuingService activityLogQueuingService;

    public T save(T model, String updateById) {
        model.setUpdatedByEntityId(updateById);
        return save(model);
    }

    public T save(T model) {
        model.setUpdatedAt(LocalDateTime.now());
        return repository.save(model);
    }

    public List<T> save(List<T> models, String updateById) {
        models.forEach(
        model -> {
            model.setUpdatedByEntityId(updateById);
            model.setUpdatedAt(LocalDateTime.now());
        });
        return repository.saveAll(models);
    }


    public void delete(T model, String deletedById) {
        model.setDeletedAt(LocalDateTime.now());
        model.setDeletedByEntityId(deletedById);
        save(model);
    }

    @Autowired
    public void setActivityLogQueuingService(ActivityLogQueuingService service) {
        this.activityLogQueuingService = service;
    }
}