package io.cart;


import io.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.now;

@Getter
@Setter
@Entity
@Table
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user-id",
            nullable = false,
            unique = true
    )
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    Instant updatedAt;

    public Cart() {

    }

    public Cart(User user) {
        this.user = user;
    }

    @PrePersist
    protected void onCreate(){
        Instant now = now();
        createdAt = now;
        updatedAt = now();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = Instant.now();
    }

    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
    }

    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);

    }

}
