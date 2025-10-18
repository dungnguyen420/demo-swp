package com.example.swp.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
@Entity
@Table(name = "trainer_profiles")
@Data

public class TrainerProfileEntity extends BaseEntity {

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
