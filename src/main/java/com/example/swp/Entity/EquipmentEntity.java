package com.example.swp.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Data
@Entity
@Table(name = "equipment")
public class EquipmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    private Integer quantity;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public EquipmentEntity() {
        this.status = Status.AVAILABLE;
    }

    private String image;

    public enum Status {
        AVAILABLE,
        BROKEN,
        MAINTENANCE
    }

}
