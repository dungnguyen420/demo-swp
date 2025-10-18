package com.example.swp.Repository;

import com.example.swp.Entity.ScheduleEntity;
import com.example.swp.Enums.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

    boolean existsByTrainerIdAndShiftAndScheduleTimeBetween(Long trainerId, Shift shift, java.time.LocalDateTime start, java.time.LocalDateTime end);

    Shift shift(Shift shift);
}
