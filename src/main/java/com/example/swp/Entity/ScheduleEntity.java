package com.example.swp.Entity;


import com.example.swp.Enums.ScheduleStatus;
import com.example.swp.Enums.Shift;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "schedules")
public class ScheduleEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false)
    private TimeSlotEntity slot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true)
    private UserEntity member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ScheduleStatus status;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "shift", length = 10)
    @Enumerated(EnumType.STRING)
    private Shift shift;

    @ManyToMany(mappedBy = "schedules")
    private java.util.List<ClassesEntity> classes = new ArrayList<>();;



}

