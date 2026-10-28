package io.internaluser.repository;

import io.internaluser.entity.InternalUser;
import io.internaluser.entity.InternalUserAuthGroup;
import io.lib.repository.BaseJpaRepository;
import io.userpermission.model.AuthGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternalUserAuthGroupRepository extends BaseJpaRepository<InternalUserAuthGroup> {
    List<InternalUserAuthGroup> findByInternalUser(InternalUser internalUser);
    List<InternalUserAuthGroup> findByAuthGroup(AuthGroup authGroup);

    Page<InternalUserAuthGroup> findByAuthGroup(AuthGroup authGroup, Pageable pageable);
}
