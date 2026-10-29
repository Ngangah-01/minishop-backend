package io.user.service;

import io.lib.service.BaseJpaRepoReadService;
import io.user.entity.User;
import io.user.repository.UserRepository;

public abstract class BaseUserReadService<U extends User, R extends UserRepository<U>> extends BaseJpaRepoReadService<U, R> {

    public boolean existsByPhoneNumber(String phoneNumber){
        var spec = repository.notDeleted()
            .and(repository.phoneNumberIs(phoneNumber));
        return repository.exists(spec);
    }

    public boolean existsByEmail(String email){
        var spec = repository.notDeleted()
                .and(repository.emailIs(email));
        return repository.exists(spec);
    }
}
