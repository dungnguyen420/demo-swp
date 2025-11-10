package com.example.swp.Entity;

import com.example.swp.Enums.SlotNumber;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "trainer_slot_prices", uniqueConstraints = {
        // Đảm bảo một HLV chỉ có 1 giá cho 1 loại slot
        @UniqueConstraint(columnNames = {"user_id", "slot_number"})
})
public class TrainerSlotPriceEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity trainer;

    @Enumerated(EnumType.STRING)
    @Column(name = "slot_number", nullable = false)
    private SlotNumber slotNumber;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
}