package io.orgbranch.service;

import io.lib.form.BaseFetchForm;
import io.lib.service.BaseJpaRepoReadService;
import io.orgbranch.entity.OrgBranch;
import io.orgbranch.repository.OrgBranchRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class OrgBranchReadService extends BaseJpaRepoReadService<OrgBranch, OrgBranchRepository> {

    public Page<OrgBranch> listOrgBranches(BaseFetchForm form){
        return repository.findAll(createSpecification(form),repository.defaultPageable(form));
    }

    private Specification<OrgBranch> createSpecification(BaseFetchForm form){
        var spec = repository.notDeleted();

        if(StringUtils.isNotBlank(form.getQuery())){
            spec = spec.and(repository.nameLike(form.getQuery()));
        }
        return spec;
    }

    public OrgBranch findByOrgBranchCode(String orgBranchName){
        var spec = repository.notDeleted().and(repository.branchCodeIs(orgBranchName));
        return repository.findOne(spec).orElse(null);
    }
}
