package com.example.swp.Repository;

import com.example.swp.Entity.EquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Long> {
    // Tìm thiết bị theo tên (có thể partial match)
    List<EquipmentEntity> findByNameContainingIgnoreCase(String name);

    List<EquipmentEntity> findByStatus(EquipmentEntity.Status status);

    List<EquipmentEntity> findByQuantity(Integer quantity);

    List<EquipmentEntity> findByPurchaseDate(LocalDate purchaseDate);
}
