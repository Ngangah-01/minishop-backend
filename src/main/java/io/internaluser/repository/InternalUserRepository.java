package io.internaluser.repository;


import io.internaluser.entity.InternalUser;
import io.orgbranch.entity.OrgBranch;
import io.user.repository.UserRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface InternalUserRepository extends UserRepository<InternalUser> {

    default Specification<InternalUser> orgBranchIs(OrgBranch orgBranch) {
        return (root, cq, cb) -> cb.equal(root.get("orgBranch"), orgBranch);
    }

    default Specification<InternalUser> departmentNameLike(String name) {
        return (root, cq, cb) -> cb.like(
                root.get("orgDepartment").get("name"), "%" + name + "%"
        );
    }

    default Specification<InternalUser> orgDepartmentIdIs(String departmentId) {
        return (root, cq, cb) -> cb.like(
                root.get("orgDepartment").get("entityId"), departmentId
        );
    }

    default Specification<InternalUser> salesPersonCodeIs(String salesPersonCode) {
        return (root, cq, cb) -> cb.like(
                root.get("erpSalesPersonCode"), salesPersonCode
        );
    }
}