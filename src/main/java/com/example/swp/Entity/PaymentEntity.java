package com.example.swp.Entity;

import com.example.swp.Enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    private String orderCode;
    private Timestamp expiredAt;
    private Long amount;
    private String description;
    private String checkoutUrl;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;
    private String qrCode;

    @OneToOne(mappedBy = "paymentEntity",
            cascade = CascadeType.ALL, orphanRemoval = true
    )
    @JsonBackReference
    private OrderEntity orderEntity;

}

