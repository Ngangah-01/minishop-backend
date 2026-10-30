package io.productcatalog.service;

import io.lib.service.BaseJpaRepoReadService;
import io.productcatalog.entity.ProductCategory;
import io.productcatalog.repository.ProductCategoryRepository;
import io.lib.form.BaseFetchForm;
import io.lib.service.BaseJpaRepoReadService;
import io.micrometer.common.util.StringUtils;
import io.productcatalog.entity.ProductCategory;
import io.productcatalog.repository.ProductCategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryReadService extends BaseJpaRepoReadService<ProductCategory, ProductCategoryRepository> {

    public Page<ProductCategory> listProductCategories(BaseFetchForm form){
        var spec = repository.notDeleted();
        if(StringUtils.isNotBlank(form.getQuery())){
            spec = spec.and(repository.nameLike(form.getQuery()));
        }

        return repository.findAll(spec, repository.defaultPageable(form));
    }

}
