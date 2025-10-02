package com.example.swp.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "system_logs")
@Data
public class SystemLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(length = 255)
    private String action;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;
}

