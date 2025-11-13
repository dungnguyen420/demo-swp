package com.example.swp.Repository;


import com.example.swp.Entity.OrderEntity;
import com.example.swp.Enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByOrderCode(String orderCode);
    Page<OrderEntity> findByUserEntityIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    long countByStatus(OrderStatus status);
    @Query("SELECT SUM(o.totalPrice) FROM OrderEntity o WHERE o.status = :status")
    Double sumTotalPriceByStatus(@Param("status") OrderStatus status);
    Page<OrderEntity> findAll(Specification<OrderEntity> spec, Pageable pageable);


}
