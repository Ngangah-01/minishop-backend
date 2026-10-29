package io.orgdepartment.service;

import io.lib.form.BaseFetchForm;
import io.lib.service.BaseJpaRepoEditService;
import io.orgdepartment.entity.OrgDepartment;
import io.orgdepartment.repository.OrgDepartmentRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class OrgDepartmentReadService extends BaseJpaRepoEditService<OrgDepartment, OrgDepartmentRepository> {
    public Page<OrgDepartment> listOrgDepartments(BaseFetchForm form){
        Specification<OrgDepartment> spec = repository.notDeleted();
        if(StringUtils.isNotBlank(form.getQuery())){
            spec = spec.and(repository.nameLike(form.getQuery()));
        }

        return repository.findAll(spec, repository.defaultPageable(form));
    }
}
