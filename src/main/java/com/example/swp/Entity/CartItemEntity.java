package com.example.swp.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cart_items")
@IdClass(CartItemId.class)
public class CartItemEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cart;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private Integer quantity;

}
