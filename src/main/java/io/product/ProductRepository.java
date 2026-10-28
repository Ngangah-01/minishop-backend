package io.product;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String keyword);
    long countByCategoryId(Long categoryId);
    boolean existsBySku(String sku);
    List<Product> findByCategoryId(Long categoryId);


}
