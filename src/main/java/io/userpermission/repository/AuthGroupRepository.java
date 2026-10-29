package io.userpermission.repository;


import io.lib.repository.BaseJpaRepository;
import io.userpermission.model.AuthGroup;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthGroupRepository extends BaseJpaRepository<AuthGroup> {

    boolean existsByNameAndEntityIdNot(String name, String entityId);

    default Specification<AuthGroup> nameIs(String name){
        return (root, cb, cq) -> cq.equal(root.get("name"), name);
    }

    default Specification<AuthGroup> nameLike(String keyWord){
        return (root, cb, cq) -> cq.like(root.get("name"), "%"+keyWord+"%");
    }

    default Specification<AuthGroup> descriptionLike(String keyWord){
        return (root, cb, cq) -> cq.like(root.get("description"), "%"+keyWord+"%");
    }
}
