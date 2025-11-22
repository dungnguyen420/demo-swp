package com.example.swp.Repository;

import com.example.swp.Entity.EquipmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Long> {
    List<EquipmentEntity> findByNameContainingIgnoreCase(String name);

    List<EquipmentEntity> findByStatus(EquipmentEntity.Status status);

    @Query("""
           SELECT e FROM EquipmentEntity e
           WHERE (:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')))
             AND (:quantityMin IS NULL OR e.quantity >= :quantityMin)
             AND (:quantityMax IS NULL OR e.quantity <= :quantityMax)
             AND (:startDate IS NULL OR e.purchaseDate >= :startDate)
             AND (:endDate IS NULL OR e.purchaseDate <= :endDate)
             AND (:status IS NULL OR e.status = :status)
           """)
    Page<EquipmentEntity> searchAdvanced(
            @Param("name") String name,
            @Param("quantityMin") Integer quantityMin,
            @Param("quantityMax") Integer quantityMax,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") EquipmentEntity.Status status,
            Pageable pageable
    );
}
