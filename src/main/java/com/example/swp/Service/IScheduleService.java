package com.example.swp.Service;

import com.example.swp.Entity.ScheduleEntity;
import com.example.swp.Entity.UserEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface IScheduleService {
    ScheduleEntity createSchedule(UserEntity trainer, LocalDate date, int slotNum);
}