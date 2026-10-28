package io.order;

import io.product.Product;
import io.product.ProductVariant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;

    @Column(nullable = false, length = 100)
    private String productName;

    @Column(length = 50)
    private String variantSize;

    @Column(length = 50)
    private String variantColor;

    @Column(length = 100)
    private String variantSku;

    @Column(nullable = false)
    private Integer quantity;

    // Snapshot: what the customer actually paid per unit, frozen at purchase time.
    @Column(nullable = false)
    private BigDecimal priceAtPurchase;

    public OrderItem() {
    }

    public OrderItem(Product product, Integer quantity) {
        this(product, null, quantity);
    }

    public OrderItem(Product product, ProductVariant variant, Integer quantity) {
        this.product = product;
        this.variant = variant;
        this.productName = product.getName();
        this.variantSize = variant == null ? null : variant.getSize();
        this.variantColor = variant == null ? null : variant.getColor();
        this.variantSku = variant == null ? null : variant.getSku();
        this.quantity = quantity;
        this.priceAtPurchase = product.getPrice();
    }

}