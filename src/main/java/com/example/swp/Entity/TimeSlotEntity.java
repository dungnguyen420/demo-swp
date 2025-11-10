package com.example.swp.Entity;

import com.example.swp.Enums.Shift;
import com.example.swp.Enums.SlotNumber;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "time_slots",
        uniqueConstraints = @UniqueConstraint(columnNames = {"slot_date","slot_number"}))
public class TimeSlotEntity extends BaseEntity {

    @Column(name = "slot_date", nullable = false)
    private LocalDate slotDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "slot_number", nullable = false, length = 8) // SLOT_1..SLOT_6
    private SlotNumber slotNumber;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "shift", nullable = false, length = 10)
    private Shift shift;

    @OneToMany(mappedBy = "slot")
    private List<ScheduleEntity> schedules = new ArrayList<>();
}


