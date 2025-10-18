//package com.example.swp.Service.impl;
//
//import com.example.swp.Entity.ClassesEntity;
//import com.example.swp.Entity.UserEntity;
//import com.example.swp.Enums.UserRole;
//import com.example.swp.Repository.ClassesRepository;
//import com.example.swp.Repository.ScheduleRepository;
//import com.example.swp.Repository.TimeSlotRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class ClassBookingService {
//
//    private final TimeSlotRepository slotRepo;
//    private final ScheduleRepository scheduleRepo;
//    private final ClassesRepository classesRepo;
//    private final UserRepository userRepo;
//
//    public ClassesEntity createClassInSlots(CreateClassDTO dto, UserEntity actor) {
//
//        Long trainerId = actor.getRole() == UserRole.TRAINER ? actor.getId() : dto.getTrainerId();
//        UserEntity trainer = userRepo.findById(trainerId)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy trainer"));
//
//        ClassesEntity clazz = new ClassesEntity();
//        clazz.setTrainer(trainer);
//        clazz.setName(dto.getName().trim());
//        clazz.setDescription(dto.getDescription());
//        clazz.setCapacity(dto.getCapacity());
//
//        List<ScheduleEntity> schedules = new ArrayList<>();
//
//        for (LocalDateTime dt : dto.getScheduleTimes()) {
//            // Ép về slot 2 giờ
//            TimeSlotEntity slot = upsertSlot(dt);
//
//            // Giới hạn: mỗi trainer chỉ có 1 schedule trong cùng slot
//            boolean trainerBusy = scheduleRepo.existsByTrainer_IdAndSlot_Id(trainer.getId(), slot.getId());
//            if (trainerBusy) {
//                throw new RuntimeException("Trainer đã có lịch trong slot: " + slot.getStartTime() + "-" + slot.getEndTime());
//            }
//
//            ScheduleEntity s = new ScheduleEntity();
//            s.setTrainer(trainer);
//            s.setSlot(slot);
//            s.setStatus(ScheduleStatus.CONFIRMED); // hoặc PENDING
//            schedules.add(s);
//        }
//
//        clazz.setSchedules(schedules);
//        return classesRepo.save(clazz);
//    }
//
//    private TimeSlotEntity upsertSlot(LocalDateTime dt) {
//        LocalDate date = dt.toLocalDate();
//        LocalTime start = snapToSlotStart(dt.toLocalTime()); // 06:00, 08:00, 10:00, ...
//        LocalTime end = start.plusHours(2);
//        Shift shift = (start.getHour() < 12) ? Shift.MORNING : Shift.AFTERNOON;
//
//        return slotRepo.findBySlotDateAndStartTimeAndEndTime(date, start, end)
//                .orElseGet(() -> {
//                    TimeSlotEntity slot = new TimeSlotEntity();
//                    slot.setSlotDate(date);
//                    slot.setStartTime(start);
//                    slot.setEndTime(end);
//                    slot.setShift(shift);
//                    return slotRepo.save(slot);
//                });
//    }
//
//    private LocalTime snapToSlotStart(LocalTime t) {
//        // ép về mốc 2h gần nhất trong khung 6..18
//        int h = t.getHour();
//        if (h < 6 || h >= 18) throw new RuntimeException("Chỉ hỗ trợ 06:00–18:00");
//        int snapped = h - (h % 2); // 7->6, 8->8, 11->10, ...
//        if (snapped < 6) snapped = 6;
//        return LocalTime.of(snapped, 0);
//    }
//}
//
