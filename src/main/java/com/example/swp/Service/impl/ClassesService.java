package com.example.swp.Service.impl;


import com.example.swp.DTO.ClassesDTO;
import com.example.swp.Entity.ClassesEntity;
import com.example.swp.Entity.ScheduleEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.Shift;
import com.example.swp.Enums.UserRole;
import com.example.swp.Repository.ClassesRepository;
import com.example.swp.Repository.ScheduleRepository;
import com.example.swp.Repository.TrainerRepository;
import com.example.swp.Service.IClassesService;
import com.example.swp.Service.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClassesService implements IClassesService {
    private static final int MAX_STUDENT = 8;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private IScheduleService scheduleService;


    @Override
    public ClassesEntity createClasses(ClassesDTO dto) {
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser.getRole() != UserRole.TRAINER && currentUser.getRole() != UserRole.MANAGER) {
            throw new RuntimeException("không có quyền tạo lớp");
        }

        Long trainerId = currentUser.getRole() == UserRole.TRAINER ? currentUser.getId() : dto.getTrainerId();
        if (trainerId == null)
            {throw new RuntimeException("trainerId bắt buộc với Manager");}

        UserEntity trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("không tìm thấy trainer"));

        if (trainer.getRole() != UserRole.TRAINER)
            {throw new RuntimeException("Người dùng chỉ định không phải trainer");}

        ClassesEntity classes = new ClassesEntity();
        classes.setTrainer(trainer);
        classes.setName(dto.getName().trim());
        classes.setDescription(dto.getDescription().trim());
        classes.setCapacity(dto.getCapacity());

        List<ScheduleEntity> scheduleList = new ArrayList<>();

        for (LocalDateTime scheduleTime : dto.getScheduleTimes()) {
            int hour = scheduleTime.getHour();
            Shift shift;
            if (hour >= 6 && hour < 12) {
                shift = Shift.MORNING;
            } else if (hour >= 12 && hour < 18) {
                shift = Shift.AFTERNOON;
            } else {
                throw new RuntimeException("Chỉ lập lịch buổi sáng và chiều");
            }

            LocalDate date = scheduleTime.toLocalDate();
            LocalDateTime startOfShift = date.atTime(shift == Shift.MORNING ? 6 : 12, 0);
            LocalDateTime endOfShift = date.atTime(shift == Shift.MORNING ? 12 : 18, 0);

            if (scheduleRepository.existsByTrainerIdAndShiftAndScheduleTimeBetween(trainer.getId(), shift, startOfShift, endOfShift)) {
                throw new RuntimeException("Trainer đã có lịch trong ca này: " + scheduleTime);
            }

            ScheduleEntity schedule = scheduleService.createSchedule(
                    trainer,
                    currentUser,
                    scheduleTime,
                    dto.getDurationMinutes()
            );
            scheduleList.add(schedule);
        }

        classes.setSchedules(scheduleList);
        return classesRepository.save(classes);
    }
}
