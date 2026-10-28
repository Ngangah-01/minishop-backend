package io.userpermission.repository;

import io.lib.repository.BaseJpaRepository;
import io.userpermission.model.UserPermission;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPermissionRepository extends BaseJpaRepository<UserPermission> {
    default Specification<UserPermission> permissionNameLike(String keyWord){
        return (root, cb, cq) -> cq.like(root.get("permissionName"), "%"+keyWord+"%");
    }

    default Specification<UserPermission> descriptionLike(String keyWord){
        return (root, cb, cq) -> cq.like(root.get("description"), "%"+keyWord+"%");
    }
}
