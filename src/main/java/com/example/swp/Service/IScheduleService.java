package com.example.swp.Service;

import com.example.swp.DTO.SlotRequest;
import com.example.swp.Entity.ScheduleEntity;
import com.example.swp.Entity.UserEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IScheduleService {
    ScheduleEntity createSchedule(UserEntity trainer, LocalDate date, int slotNum);
    List<ScheduleEntity> getSchedulesByTrainer(Long trainerId);
    void bookPrivateSessions(Long trainerId, Long memberId, List<SlotRequest> slots);}