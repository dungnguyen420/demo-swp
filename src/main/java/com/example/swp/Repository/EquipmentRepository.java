package com.example.swp.Repository;

import com.example.swp.Entity.EquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Long> {
    // Tìm theo trạng thái (ví dụ: tất cả thiết bị đang bảo trì)
    List<EquipmentEntity> findByStatus(EquipmentEntity.Status status);
}
