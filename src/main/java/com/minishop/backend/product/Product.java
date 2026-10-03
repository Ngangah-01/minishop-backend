package com.minishop.backend.product;

import com.minishop.backend.category.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "products",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "sku")
        })
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false,precision = 10, scale = 2)// upto 10 digits, 2 decimal places
    private BigDecimal price;

    @Column(nullable = false, unique = true)
    private String sku; // Stock Keeping Unit, unique identifier for each product

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private Integer stockQuantity;

    //createdAt and updatedAt fields for tracking when the product was created and last updated
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    //no args constructor for JPA
    public Product() {
    }

    // parameterized constructor for creating a new product
    public Product(String name, String description, BigDecimal price,Category category,Integer stockQuantity, String sku) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.stockQuantity = stockQuantity;
        this.sku = sku;

    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // Getters and Setters

}
