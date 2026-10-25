package com.minishop.backend.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_variants")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(length = 50)
    private String size;

    @Column(length = 50)
    private String color;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private Integer stockQuantity;

    public ProductVariant() {
    }

    public ProductVariant(String size, String color, String sku, Integer stockQuantity) {
        this.size = size;
        this.color = color;
        this.sku = sku;
        this.stockQuantity = stockQuantity;
    }
}
