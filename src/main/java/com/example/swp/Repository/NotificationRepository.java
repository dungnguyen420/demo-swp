package com.example.swp.Repository;

import com.example.swp.Entity.NotificationEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUser(UserEntity user);
    List<NotificationEntity> findByUserAndStatus(UserEntity user, NotificationStatus status);
}
