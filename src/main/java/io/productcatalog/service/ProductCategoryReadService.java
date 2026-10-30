package io.productcatalog.service;

import io.lib.service.BaseJpaRepoReadService;
import io.productcatalog.entity.ProductCategory;
import io.productcatalog.repository.ProductCategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryReadService extends BaseJpaRepoReadService<ProductCategory, ProductCategoryRepository> {

}
