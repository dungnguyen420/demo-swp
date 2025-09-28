package com.example.swp.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Entity
@Table(name = "member_profiles")
public class MemberProfileEntity {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(length = 255)
    private String targetGoal;

    @Column(precision = 5, scale = 2)
    private BigDecimal initialWeight;

    @Column(precision = 5, scale = 2)
    private BigDecimal initialHeight;

    @Column(columnDefinition = "TEXT")
    private String initialHealthNotes;


}

