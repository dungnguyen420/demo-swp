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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByOrderCode(String orderCode);
    Page<OrderEntity> findByUserEntityIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    long countByStatus(OrderStatus status);
    @Query("SELECT SUM(o.totalPrice) FROM OrderEntity o WHERE o.status = :status")
    Page<OrderEntity> findAll(Specification<OrderEntity> spec, Pageable pageable);
    //doanh thu tong
    @Query("""
        SELECT COALESCE(SUM(o.totalPrice), 0)
        FROM OrderEntity o
        WHERE o.status = com.example.swp.Enums.OrderStatus.PAID
        """)
    Long getTotalRevenue();


    //doanh thu 7 day gan nhat
    @Query(value = """
        SELECT DATE(o.created_at) AS day,
               COALESCE(SUM(o.total_price), 0) AS revenue
        FROM orders o
        WHERE o.status = 'PAID'
          AND o.created_at >= DATE_SUB(CURDATE(), INTERVAL 6 DAY)
        GROUP BY DATE(o.created_at)
        ORDER BY day
        """, nativeQuery = true)
    List<Object[]> getRevenueLast7DaysRaw();


    @Query("""
        SELECT COALESCE(SUM(o.totalPrice), 0)
        FROM OrderEntity o
        WHERE o.status = com.example.swp.Enums.OrderStatus.PAID
          AND o.createdAt BETWEEN :start AND :end
        """)
    Long getRevenueBetween(@Param("start") LocalDateTime start,
                           @Param("end") LocalDateTime end);

    @Query("""
    SELECT COALESCE(SUM(o.totalPrice), 0)
    FROM OrderEntity o
    WHERE o.status = com.example.swp.Enums.OrderStatus.PAID
      AND (:username IS NULL OR :username = '' OR o.userEntity.userName = :username)
      AND (:start IS NULL OR o.createdAt >= :start)
      AND (:end   IS NULL OR o.createdAt <  :end)
""")
    Long getRevenueAdvanced(@Param("start") LocalDateTime start,
                            @Param("end")   LocalDateTime end,
                            @Param("username") String username);
}
