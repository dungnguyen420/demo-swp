package com.example.swp.controller;

import com.example.swp.Entity.NotificationEntity;
import com.example.swp.Service.impl.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // --- Gửi thông báo ---
    @PostMapping("/send/{userId}")
    public NotificationEntity sendNotification(
            @PathVariable Long userId,
            @RequestBody NotificationEntity notification) {
        return notificationService.sendNotification(userId, notification);
    }

    // --- Lấy tất cả thông báo của user ---
    @GetMapping("/user/{userId}")
    public List<NotificationEntity> getNotifications(@PathVariable Long userId) {
        return notificationService.getNotifications(userId);
    }

    // --- Đánh dấu đã đọc ---
    @PutMapping("/{id}/read")
    public NotificationEntity markAsRead(@PathVariable Long id) {
        return notificationService.markAsRead(id);
    }

    // --- Xóa thông báo ---
    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
    }
}
