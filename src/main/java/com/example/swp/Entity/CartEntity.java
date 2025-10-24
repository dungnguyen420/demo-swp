package com.example.swp.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cart")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private double totalPrice;

    private long paymentId;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Version
    @Column(name = "version")
    private Integer version;

    public CartEntity() {}

    public CartEntity(UserEntity user) {
        this.user = user;
    }
}
