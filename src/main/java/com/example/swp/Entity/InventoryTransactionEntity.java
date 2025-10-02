package com.example.swp.Entity;



import com.example.swp.Enums.InventoryTransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inventory_transactions")
public class InventoryTransactionEntity extends BaseEntity {


    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InventoryTransactionType type;

    @Column(name = "quantity_change", nullable = false)
    private Integer quantityChange;

    @Column(name = "reference_doc", length = 100)
    private String referenceDoc;

    @Column(name = "created_at", updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}

