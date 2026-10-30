package io.productcatalog.repository;

import io.lib.repository.BaseJpaRepository;
import io.productcatalog.entity.ProductCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends BaseJpaRepository<ProductCategory> {
import org.springframework.data.jpa.domain.Specification;

public interface ProductCategoryRepository extends BaseJpaRepository<ProductCategory> {
    default Specification<ProductCategory> nameIs(String name){
        return (root, cb, cq) -> cq.equal(root.get("name"), name);
    }

    default Specification<ProductCategory> nameLike(String query){
        return (root, cq, cb) -> cb.like(root.get("name"), "%" + query + "%");
    }

}
