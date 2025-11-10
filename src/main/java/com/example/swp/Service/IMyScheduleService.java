package com.example.swp.Service;

import com.example.swp.Entity.ScheduleEntity;

import java.util.List;

public interface IMyScheduleService {
    List<ScheduleEntity> getPersonalSchedules(Long userId, String mode); // Lịch PT của Member
    List<ScheduleEntity> getClassSchedules(Long userId, String mode);    // Lịch Lớp của Member
    List<ScheduleEntity> getTeachingSchedules(Long userId, String mode); // Lịch Dạy của Trainer
}