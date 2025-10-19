package com.example.swp.Repository;

import com.example.swp.Entity.CartItemEntity;
import com.example.swp.Entity.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemEntity, CartItemId> {
    List<CartItemEntity> findByCart_UserId(Long userId);
    Optional<CartItemEntity> findByCart_UserIdAndProduct_Id(Long userId, Long productId);
    boolean existsByCart_UserIdAndProduct_Id(Long userId, Long productId);
}