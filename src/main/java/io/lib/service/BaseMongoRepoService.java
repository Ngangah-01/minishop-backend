package io.lib.service;

import io.lib.entity.BaseMongoEntity;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.repository.BaseMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public abstract class BaseMongoRepoService<T extends BaseMongoEntity, R extends BaseMongoRepository<T>> {
    protected R repository;
    protected MongoTemplate mongoTemplate;

    public T save(T model, String updateById) {
        model.setUpdatedByEntityId(updateById);
        return save(model);
    }

    public T save(T model) {
        model.setUpdateAt(LocalDateTime.now());
        return repository.save(model);
    }

    public List<T> save(List<T> models, String updateById) {
        models.forEach(
                model -> {
                    model.setUpdatedByEntityId(updateById);
                    model.setUpdateAt(LocalDateTime.now());
                });
        return repository.saveAll(models);
    }

    public T findByEntityId(String id) {
        Optional<T> entityOpt =  repository.findById(id);
        if (entityOpt.isEmpty()) {
            throw new CommonRuntimeException(
                    ExceptionType.NOT_FOUND,
                    "error.entity.not.found"
            );
        }
        return entityOpt.get();
    }


    @Autowired
    public void setRepository(R repository) {
        this.repository = repository;
    }


    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
