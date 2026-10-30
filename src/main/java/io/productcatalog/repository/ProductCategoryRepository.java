package io.productcatalog.repository;

import io.lib.repository.BaseJpaRepository;
import io.productcatalog.entity.ProductCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends BaseJpaRepository<ProductCategory> {
}
