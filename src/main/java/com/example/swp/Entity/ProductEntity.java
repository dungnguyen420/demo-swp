package com.example.swp.Entity;



import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private double price;

    @Column(nullable = false)
    private  int quantity;

    private String image;

    @Version
    @Column(name = "version")
    private Integer version;
}

