package com.example.swp.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "cart_items")

public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cart;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = true)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = true)
    private PackageEntity packageEntity;

    public String getDisplayName() {
        if (product != null) return product.getName();
        if (packageEntity != null) return packageEntity.getName();
        return "N/A";
    }


    public double getDisplayPrice() {
        if (product != null) return product.getPrice();
        if (packageEntity != null) return packageEntity.getPrice();
        return 0.0;
    }

    @Column(nullable = false)
    private Integer quantity;

}