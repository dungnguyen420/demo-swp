package com.example.swp.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;


    @Column(nullable = false, precision = 12)
    private double price;

    @Column(name = "duration_month", nullable = false)
    private Integer durationMonth;

    @Column(name = "pt_sessions_count", nullable = false)
    private Integer ptSessionsCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_level", length = 20)
    private AccessLevel accessLevel = AccessLevel.BASIC;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();


    @OneToMany(mappedBy = "packageEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubscriptionEntity> subscriptions;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public enum AccessLevel {
        BASIC, STANDARD, PREMIUM
    }
}
