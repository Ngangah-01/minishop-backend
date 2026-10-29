package io.userpermission.repository;


import io.lib.repository.BaseJpaRepository;
import io.userpermission.model.AuthGroup;
import io.userpermission.model.AuthGroupPermission;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthGroupPermissionRepository extends BaseJpaRepository<AuthGroupPermission> {
    default Specification<AuthGroupPermission> authGroupIdIn(List<AuthGroup> groups){
        return (root, query, builder) -> root.get("authGroup").in(groups);
    }
}
