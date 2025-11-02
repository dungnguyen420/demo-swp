package com.example.swp.Service.impl;


import com.example.swp.DTO.ClassesDTO;
import com.example.swp.DTO.CreateClassBySlotDTO;
import com.example.swp.DTO.CreateClassDTO;
import com.example.swp.DTO.SlotRequest;
import com.example.swp.Entity.*;
import com.example.swp.Enums.Shift;
import com.example.swp.Enums.UserGender;
import com.example.swp.Enums.UserRole;
import com.example.swp.Repository.*;
import com.example.swp.Service.IClassesService;
import com.example.swp.Service.IScheduleService;
import com.example.swp.Service.IUserService;
import jakarta.transaction.Transactional;
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
    private final ClassMemberRepository memberRepo;
    private final ScheduleRepository scheduleRepo;


    private static final int MAX_CAPACITY_DEFAULT = 8;


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

        List<SlotRequest> reqs = mergePickOne(dto);
        if (reqs.isEmpty()) throw new IllegalArgumentException("Chưa chọn slot hợp lệ");

        ClassesEntity clazz = new ClassesEntity();
        clazz.setTrainer(trainer);
        clazz.setName(dto.getName().trim());
        clazz.setDescription(dto.getDescription());
        clazz.setCapacity(dto.getCapacity());

        List<ScheduleEntity> schedules = new ArrayList<>();
        for (SlotRequest r : reqs) {
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
    public Page<ClassesEntity> listUpcoming(int page, int size, String sortBy, String dir) {
        if (page < 0) page = 0;
        if (size <= 0 || size > 100) size = 10;
        if (sortBy == null || sortBy.isBlank()) sortBy = "createdAt";
        Sort sort = "asc".equalsIgnoreCase(dir) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        LocalDate today = LocalDate.now();
        return classesRepo.findDistinctBySchedules_Slot_SlotDateGreaterThanEqual(today, pageable);
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

    @Override
    public ClassesEntity updateBasicInfo(Long classId, String name, String description, Integer capacity) {
        ClassesEntity c = classesRepo.findById(classId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp"));
        if (name != null && !name.isBlank()) c.setName(name.trim());
        c.setDescription(description);
        if (capacity != null && capacity > 0) c.setCapacity(capacity);
        return classesRepo.save(c);
    }

    @Override
    public void deleteById(Long classId) {
        if (!classesRepo.existsById(classId)) return;
        classesRepo.deleteById(classId);
    }

    @Transactional
    @Override
    public void register(Long classId, Long userId) {
        ClassesEntity clazz = classesRepo.findById(classId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp"));
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        // Đã đăng ký?
        if (memberRepo.existsById_ClassIdAndId_UserId(classId, userId)) {
            throw new RuntimeException("Bạn đã đăng ký lớp này");
        }

        boolean noFutureSchedules = clazz.getSchedules() == null
                || clazz.getSchedules().stream()
                .noneMatch(s -> s.getSlot() != null && s.getSlot().getSlotDate() != null
                        && !s.getSlot().getSlotDate().isBefore(LocalDate.now()));
        if (noFutureSchedules) {
            throw new RuntimeException("Lớp không còn lịch học trong tương lai, không thể đăng ký");
        }

        // Kiểm tra sức chứa
        int capacity = clazz.getCapacity() != null ? clazz.getCapacity() : MAX_CAPACITY_DEFAULT;
        long current = memberRepo.countByClassEntity_Id(classId);
        if (current >= capacity) {
            throw new RuntimeException("Lớp đã đủ chỗ (" + capacity + ")");
        }

        // Kiểm tra trùng slot với các lớp khác của user
        List<Long> slotIds = clazz.getSchedules()
                .stream().map(s -> s.getSlot().getId()).distinct().toList();

        if (!slotIds.isEmpty()) {
            boolean busy = memberRepo.existsById_UserIdAndClassEntity_Schedules_Slot_IdIn(userId, slotIds);
            if (busy) throw new RuntimeException("Bạn đã có lịch trong các slot này");
        }


        // Lưu tham gia
        ClassMemberId id = new ClassMemberId(classId, userId);
        ClassMember cm = new ClassMember();
        cm.setId(id);
        cm.setClassEntity(clazz);
        cm.setMember(user);
        memberRepo.save(cm);
    }

    @Transactional
    @Override
    public void unregister(Long classId, Long userId) {
        ClassMemberId id = new ClassMemberId(classId, userId);
        if (!memberRepo.existsById(id)) return;
        memberRepo.deleteById(id);
    }

    private List<SlotRequest> mergePickOne(CreateClassBySlotDTO dto) {
        var map = new java.util.LinkedHashMap<String, SlotRequest>();

        // ưu tiên danh sách slots
        if (dto.getSlots() != null)
            for (var s : dto.getSlots())
                if (s != null && s.getDate() != null && s.getSlotNumber() != null)
                    map.putIfAbsent(s.getDate() + "#" + s.getSlotNumber(), s);

        // bổ sung từ dates + slotNumbers nếu key chưa có
        if (dto.getDates() != null && dto.getSlotNumbers() != null)
            for (int i = 0, n = Math.min(dto.getDates().size(), dto.getSlotNumbers().size()); i < n; i++) {
                var d = dto.getDates().get(i); var sn = dto.getSlotNumbers().get(i);
                if (d != null && sn != null) map.putIfAbsent(d + "#" + sn, new SlotRequest(d, sn));
            }

        return new java.util.ArrayList<>(map.values());
    }

    public Page<ClassesEntity> search(String className, String trainerLast, String genderStr,
                                      String mode, Pageable pageable) {
        UserGender gender = null;
        if (genderStr != null && !genderStr.isBlank()) {
            try { gender = UserGender.valueOf(genderStr); } catch (Exception ignored) {}
        }
        className   = (className == null || className.isBlank()) ? null : className;
        trainerLast = (trainerLast == null || trainerLast.isBlank()) ? null : trainerLast;
        mode = (mode == null || mode.isBlank()) ? "all" : mode; // all | upcoming | finished
        return classesRepo.search(className, trainerLast, gender, mode, pageable);
    }

    }





