package com.example.swp.Repository;

import com.example.swp.Entity.TimeSlotEntity;
import com.example.swp.Enums.Shift;
import com.example.swp.Enums.SlotNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TimeSlotRepository extends JpaRepository<TimeSlotEntity, Long> {
    Optional<TimeSlotEntity> findBySlotDateAndSlotNumber(LocalDate date, SlotNumber slotNumber);
    List<TimeSlotEntity> findAllBySlotDate(LocalDate date);
}
