package com.example.swp.Repository;

import com.example.swp.Entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long>, JpaSpecificationExecutor<OrderItemEntity> {
    boolean existsByPackageId(Long packageId);
    @Query("SELECT oi FROM OrderItemEntity oi " +
            "JOIN oi.orderEntity o " +
            "WHERE o.userEntity.id = :userId " +
            "AND oi.packageId IS NOT NULL " +
            "AND o.status = com.example.swp.Enums.OrderStatus.PAID")
    List<OrderItemEntity> findPurchasedPackagesByUser(Long userId);
}
