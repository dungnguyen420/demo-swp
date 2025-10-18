package com.example.swp.Entity;


import com.example.swp.Enums.ScheduleStatus;
import com.example.swp.Enums.Shift;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "schedules")
public class ScheduleEntity extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private UserEntity member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private UserEntity trainer;

    @Column(name = "schedule_time", nullable = false)
    private LocalDateTime scheduleTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes = 60;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ScheduleStatus status;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "shift", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Shift shift;

    @ManyToMany(mappedBy = "schedules")
    private java.util.List<ClassesEntity> classes;


}

