package com.example.swp.Entity;



import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_items")
public class OrderItemEntity extends BaseEntity {

    private String productName;
    @Column(name = "product_price", nullable = false)
    private double productPrice;
    private long productId;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private OrderEntity orderEntity;
}

