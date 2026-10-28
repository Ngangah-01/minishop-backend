package io.productcatalog.entity;


import io.lib.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProductCategory extends BaseJpaEntity {
    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1028)
    private String description;

    private String imageUrl;
}
