package io.userpermission.service;

import io.lib.service.BaseJpaRepoReadService;
import io.userpermission.form.FetchAuthGroupForm;
import io.userpermission.model.AuthGroup;
import io.userpermission.repository.AuthGroupRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AuthGroupReadService extends BaseJpaRepoReadService<AuthGroup, AuthGroupRepository> {
    public Page<AuthGroup> listGroups(FetchAuthGroupForm form){
        var spec = repository.notDeleted();

        if(!form.getQuery().isBlank()) {
            spec = spec.and(
                    repository.nameLike(form.getQuery())
                            .or(repository.descriptionLike(form.getQuery()))
            );
        }

        Pageable pageable = repository.defaultPageable(form);
        return repository.findAll(spec, pageable);
    }
}
