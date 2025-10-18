package com.example.swp.Repository;

import com.example.swp.Entity.ScheduleEntity;
import com.example.swp.Enums.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

    // Mỗi trainer chỉ 1 lịch trong cùng slot
    boolean existsByTrainer_IdAndSlot_Id(Long trainerId, Long slotId);

    // (Tuỳ chọn) tổng số lớp trong slot để giới hạn capacity slot toàn hệ thống
    long countBySlot_Id(Long slotId);
}
