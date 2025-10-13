package com.example.swp.Service.impl;


import com.example.swp.DTO.ClassesDTO;
import com.example.swp.DTO.CreateClassBySlotDTO;
import com.example.swp.DTO.CreateClassDTO;
import com.example.swp.DTO.SlotRequest;
import com.example.swp.Entity.ClassesEntity;
import com.example.swp.Entity.ScheduleEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.Shift;
import com.example.swp.Enums.UserRole;
import com.example.swp.Repository.ClassesRepository;
import com.example.swp.Repository.IUserRepository;
import com.example.swp.Repository.ScheduleRepository;
import com.example.swp.Repository.TrainerRepository;
import com.example.swp.Service.IClassesService;
import com.example.swp.Service.IScheduleService;
import com.example.swp.Service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassesService implements IClassesService {

    private final ClassesRepository classesRepo;
    private final IUserRepository userRepo;
    private final IScheduleService scheduleService;

    @Override
    public ClassesEntity createClassBySlots(CreateClassBySlotDTO dto) {

        UserEntity actor = getCurrentUser();

        if (actor.getRole() != UserRole.TRAINER && actor.getRole() != UserRole.MANAGER)
            throw new RuntimeException("Không có quyền tạo lớp");

        Long trainerId = actor.getRole() == UserRole.TRAINER ? actor.getId() : dto.getTrainerUserId();
        if (trainerId == null) throw new RuntimeException("Manager phải chọn trainer");

        UserEntity trainer = userRepo.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trainer"));
        if (trainer.getRole() != UserRole.TRAINER) throw new RuntimeException("User chỉ định không phải trainer");

        ClassesEntity clazz = new ClassesEntity();
        clazz.setTrainer(trainer);
        clazz.setName(dto.getName().trim());
        clazz.setDescription(dto.getDescription());
        clazz.setCapacity(dto.getCapacity());

        List<ScheduleEntity> schedules = new ArrayList<>();
        for (SlotRequest r : dto.getSlots()) {
            ScheduleEntity s = scheduleService.createSchedule(trainer, r.getDate(), r.getSlotNumber());
            schedules.add(s);
        }

        clazz.setSchedules(schedules);
        return classesRepo.save(clazz);
    }

    private UserEntity getCurrentUser() {
        Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (p instanceof CustomUserDetails cud) return cud.getUser();
        String username = p.toString();
        return userRepo.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
    }

    @Override
    public Page<ClassesEntity> listPaged(int page, int size, String sortBy, String dir) {
        if (page < 0) page = 0;
        if (size <= 0 || size > 100) size = 10;
        if (sortBy == null || sortBy.isBlank()) sortBy = "createdAt";
        Sort sort = "asc".equalsIgnoreCase(dir) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return classesRepo.findAll(pageable);
    }

    @Override
    public ClassesEntity getDetail(Long id) {
        return classesRepo.findWithAllById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));
    }
}


