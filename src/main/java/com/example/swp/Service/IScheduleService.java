package com.example.swp.Service;

import com.example.swp.Entity.ScheduleEntity;
import com.example.swp.Entity.UserEntity;

import java.time.LocalDateTime;

public interface IScheduleService {

    ScheduleEntity createSchedule(UserEntity trainer, UserEntity member, LocalDateTime scheduleTime, Integer durationMinutes);
}
