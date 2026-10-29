package io.orgbranch.repository;

import io.lib.repository.BaseJpaRepository;
import io.orgbranch.entity.OrgBranch;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgBranchRepository extends BaseJpaRepository<OrgBranch> {
    default Specification<OrgBranch> nameIs(String name){
        return (root, cb, cq) -> cq.equal(root.get("name"), name);
    }

    default Specification<OrgBranch> nameLike(String name){
        return (root, cq, cb) ->
                cb.like(root.get("name"), "%" + name +"%");
    }

    default Specification<OrgBranch> branchCodeIs(String branchCode){
        return (root, cb, cq) -> cq.equal(root.get("orgBranchCode"), branchCode);
    }
}
