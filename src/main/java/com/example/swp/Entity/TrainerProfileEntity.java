package com.example.swp.Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

public class TrainerProfileEntity {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String specialization;

    @Column(columnDefinition = "TEXT")
    private String certificationInfo;

    @Column(precision = 10, scale = 2)
    private BigDecimal hourlyRate = BigDecimal.ZERO;

}
