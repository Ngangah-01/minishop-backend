package io.userpermission.service;


import io.lib.form.BaseFetchForm;
import io.lib.service.BaseJpaRepoReadService;
import io.userpermission.model.UserPermission;
import io.userpermission.repository.UserPermissionRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserPermissionReadService extends BaseJpaRepoReadService<UserPermission, UserPermissionRepository> {
    public Page<UserPermission> listUserPermissions(BaseFetchForm form){
        Specification<UserPermission> spec = repository.notDeleted();

        if(StringUtils.isNotEmpty(form.getQuery())) {
            spec = spec.and(
                repository.permissionNameLike(form.getQuery())
                .or(repository.descriptionLike(form.getQuery()))
            );
        }

        var pageable = repository.defaultPageable(form);
        return repository.findAll(spec, pageable);
    }
}
