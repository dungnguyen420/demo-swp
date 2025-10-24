package com.example.swp.Entity;

import com.example.swp.Enums.PaymentMethod;
import com.example.swp.Enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private Integer amount;
    private String description;
    private String checkoutUrl;
    private String status;
    private String qrCode;

    @OneToOne(mappedBy = "paymentEntity",
            cascade = CascadeType.ALL, orphanRemoval = true
    )
    @JsonBackReference
    private OrderEntity orderEntity;

}

