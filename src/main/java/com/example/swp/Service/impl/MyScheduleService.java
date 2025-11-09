package com.example.swp.Service.impl;

import com.example.swp.Entity.ScheduleEntity;
import com.example.swp.Repository.ScheduleRepository;
import com.example.swp.Service.IMyScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyScheduleService implements IMyScheduleService {

    private final ScheduleRepository scheduleRepo;

    @Override
    public List<ScheduleEntity> getPersonalSchedules(Long userId, String mode) {
        return scheduleRepo.findAllPersonalSchedulesByMemberId(userId, mode);
    }

    @Override
    public List<ScheduleEntity> getClassSchedules(Long userId, String mode) {
        return scheduleRepo.findAllClassSchedulesByMemberId(userId, mode);
    }

    @Override
    public List<ScheduleEntity> getTeachingSchedules(Long userId, String mode) {
        return scheduleRepo.findAllTeachingSchedulesByTrainerId(userId, mode);
    }
}