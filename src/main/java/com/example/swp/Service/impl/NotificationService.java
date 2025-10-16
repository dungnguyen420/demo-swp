package com.example.swp.Service.impl;

import com.example.swp.Entity.NotificationEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.NotificationStatus;
import com.example.swp.Repository.NotificationRepository;
import com.example.swp.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private IUserRepository userRepository;

    // Gửi thông báo cho 1 user
    public NotificationEntity sendNotification(Long userId, NotificationEntity notification) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        notification.setUser(user);
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    // Lấy danh sách thông báo của user
    public List<NotificationEntity> getNotifications(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepository.findByUser(user);
    }

    // Đánh dấu thông báo đã đọc
    public NotificationEntity markAsRead(Long notificationId) {
        NotificationEntity n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        n.setStatus(NotificationStatus.READ);
        return notificationRepository.save(n);
    }

    // Xóa thông báo
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}
