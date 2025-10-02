package com.example.swp.Entity;

import com.example.swp.Enums.NotificationStatus;
import com.example.swp.Enums.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private NotificationStatus status = NotificationStatus.UNREAD;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;
}

