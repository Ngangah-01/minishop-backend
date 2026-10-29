package io.productcatalog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.lib.entity.BaseJpaEntity;
import io.lib.service.FormatUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class ProductItem extends BaseJpaEntity {

    @JsonIgnore
    @ManyToOne
    private ProductCategory productCategory;

    private String productCategoryEntityId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1028)
    private String description;

    private String imageUrl;

    @Column(precision = FormatUtil.BIG_DECIMAL_PRECISION, scale = FormatUtil.BIG_DECIMAL_SCALE)
    private BigDecimal price;

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
        if(this.productCategory != null) {
            this.productCategoryEntityId = this.productCategory.getEntityId();
        }
    }
}
