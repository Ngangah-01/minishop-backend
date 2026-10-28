package io.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, length = 2048)
    private String url;

    @Column(nullable = false)
    private Integer displayOrder;

    public ProductImage() {
    }

    public ProductImage(String url, Integer displayOrder) {
        this.url = url;
        this.displayOrder = displayOrder;
    }
}
