package com.example.swp.Service.impl;

import com.example.swp.Entity.ScheduleEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.ScheduleStatus;
import com.example.swp.Enums.Shift;
import com.example.swp.Repository.ScheduleRepository;
import com.example.swp.Service.IScheduleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ScheduleService implements IScheduleService {


    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public ScheduleEntity createSchedule(UserEntity trainer, UserEntity member, java.time.LocalDateTime scheduleTime, Integer durationMinutes) {

        if (durationMinutes == null) durationMinutes = 60;

        // Xác định ca sáng / chiều
        Shift shift;
        int hour = scheduleTime.getHour();
        if (hour >= 6 && hour < 12) {
            shift = Shift.MORNING;
        } else if (hour >= 12 && hour < 18) {
            shift = Shift.AFTERNOON;
        } else {
            throw new IllegalArgumentException("Chỉ lập lịch ca sáng hoặc chiều");
        }

        // Lấy ngày của schedule
        java.time.LocalDate date = scheduleTime.toLocalDate();
        java.time.LocalDateTime startOfShift = date.atTime(shift == Shift.MORNING ? 6 : 12, 0);
        java.time.LocalDateTime endOfShift = date.atTime(shift == Shift.MORNING ? 12 : 18, 0);

        // Kiểm tra trùng ca
        if (scheduleRepository.existsByTrainerIdAndShiftAndScheduleTimeBetween(trainer.getId(), shift, startOfShift, endOfShift)) {
            throw new IllegalStateException("Trainer đã có lớp trong ca này");
        }

        // Tạo schedule mới
        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setTrainer(trainer);
        schedule.setMember(member);
        schedule.setScheduleTime(scheduleTime);
        schedule.setDurationMinutes(durationMinutes);
        schedule.setShift(shift);
        schedule.setStatus(ScheduleStatus.PENDING);

        return scheduleRepository.save(schedule);
    }
}
