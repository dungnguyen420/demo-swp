package com.example.swp.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "progress_tracking")
@Data
public class ProgressTrackingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private UserEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private UserEntity trainer;

    @Column(name = "tracker_date", nullable = false)
    private LocalDate trackerDate;

    @Column(name = "exercise_name", length = 255)
    private String exerciseName;

    private Integer sets;

    private Integer reps;

    @Column(name = "weight_lifted", precision = 6, scale = 2)
    private BigDecimal weightLifted;

    @Column(columnDefinition = "TEXT")
    private String notes;
}

