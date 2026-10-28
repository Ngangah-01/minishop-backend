package io.orgdepartment.repository;


import io.lib.repository.BaseJpaRepository;
import io.orgdepartment.entity.OrgDepartment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgDepartmentRepository extends BaseJpaRepository<OrgDepartment> {

    default Specification<OrgDepartment> nameLike(String name){
        return (root, cq, cb) -> cb.like(root.get("name"), "%" + name +"%");
    }
}
