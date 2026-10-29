package io.lib.repository;


import io.lib.entity.BaseMongoEntity;
import io.lib.form.BaseFetchForm;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseMongoRepository<T extends BaseMongoEntity> extends MongoRepository<T, String> {

    Sort CREATED_AT_DESC = Sort.by(Sort.Direction.DESC, "createdAt");

    default Pageable defaultPageable(BaseFetchForm form, Sort sort) {
        if (sort == null) {
            return defaultPageable(form);
        } else {
            return PageRequest.of(
                    form.getPageNum(),
                    form.getPageSize(),
                    sort
            );
        }
    }

    default Pageable defaultPageable(BaseFetchForm form) {
        return PageRequest.of(
                form.getPageNum(),
                form.getPageSize(),
                CREATED_AT_DESC
        );
    }
}
