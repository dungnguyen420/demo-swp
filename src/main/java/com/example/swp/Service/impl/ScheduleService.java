package com.example.swp.Service.impl;

import com.example.swp.DTO.SlotRequest;
import com.example.swp.Entity.ScheduleEntity;
import com.example.swp.Entity.TimeSlotEntity;
import com.example.swp.Entity.TrainerSlotPriceEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.ScheduleStatus;
import com.example.swp.Enums.Shift;
import com.example.swp.Enums.SlotNumber;
import com.example.swp.Repository.*;
import com.example.swp.Service.IScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService implements IScheduleService {

    private final TimeSlotRepository slotRepo;
    private final ScheduleRepository scheduleRepo;
    private final IUserRepository userRepo;
    private final ClassMemberRepository classMemberRepo;

    // Một trainer chỉ 1 schedule trong cùng (date, slotNumber)
    public ScheduleEntity createSchedule(UserEntity trainer, LocalDate date, int slotNum) {

        SlotNumber slotNumber = SlotNumber.fromNumber(slotNum);
        Shift shift = slotNumber.number <= 3 ? Shift.MORNING : Shift.AFTERNOON;

        TimeSlotEntity slot = slotRepo.findBySlotDateAndSlotNumber(date, slotNumber)
                .orElseGet(() -> {
                    TimeSlotEntity s = new TimeSlotEntity();
                    s.setSlotDate(date);
                    s.setSlotNumber(slotNumber);
                    s.setStartTime(slotNumber.start);
                    s.setEndTime(slotNumber.end);
                    s.setShift(shift);
                    return slotRepo.save(s);
                });

        boolean busy = scheduleRepo.existsByTrainer_IdAndSlot_Id(trainer.getId(), slot.getId());
        if (busy) throw new IllegalStateException("Trainer đã có lịch ở slot " + slotNumber.number + " ngày " + date);

        ScheduleEntity sch = new ScheduleEntity();
        sch.setTrainer(trainer);
        sch.setSlot(slot);
        sch.setStatus(ScheduleStatus.CONFIRMED);
        return scheduleRepo.save(sch);
    }

    public List<ScheduleEntity> getSchedulesByTrainer(Long trainerId) {
        return scheduleRepo.findAllByTrainer_Id(trainerId);
    }

    private TimeSlotEntity upsertTimeSlot(LocalDate date, int slotNum) {
        SlotNumber slotNumber = SlotNumber.fromNumber(slotNum);
        Shift shift = slotNumber.number <= 3 ? Shift.MORNING : Shift.AFTERNOON;

        return slotRepo.findBySlotDateAndSlotNumber(date, slotNumber)
                .orElseGet(() -> {
                    TimeSlotEntity s = new TimeSlotEntity();
                    s.setSlotDate(date);
                    s.setSlotNumber(slotNumber);
                    s.setStartTime(slotNumber.start);
                    s.setEndTime(slotNumber.end);
                    s.setShift(shift);
                    return slotRepo.save(s);
                });
    }

    private ScheduleEntity bookSingleSession(UserEntity trainer, UserEntity member, LocalDate date, int slotNum) {
        // ... (Logic của hàm này đã đúng, giữ nguyên) ...

        // 1. Tìm hoặc tạo TimeSlot
        TimeSlotEntity slot = upsertTimeSlot(date, slotNum);
        Long slotId = slot.getId();

        // 2. KIỂM TRA LỊCH TRÙNG CỦA TRAINER
        boolean trainerBusy = scheduleRepo.existsByTrainer_IdAndSlot_Id(trainer.getId(), slotId);
        if (trainerBusy) {
            throw new IllegalStateException("HLV bận vào Slot " + slotNum + " ngày " + date);
        }

        // 3. KIỂM TRA LỊCH TRÙNG CỦA MEMBER
        boolean memberBusyPT = scheduleRepo.existsByMember_IdAndSlot_Id(member.getId(), slotId);
        if (memberBusyPT) {
            throw new IllegalStateException("Bạn đã có lịch PT vào Slot " + slotNum + " ngày " + date);
        }
        boolean memberBusyClass = classMemberRepo.existsById_UserIdAndClassEntity_Schedules_Slot_Id(member.getId(), slotId);
        if (memberBusyClass) {
            throw new IllegalStateException("Bạn đã có lịch Lớp học vào Slot " + slotNum + " ngày " + date);
        }

        // 4. Tạo và lưu Schedule
        ScheduleEntity sch = new ScheduleEntity();
        sch.setTrainer(trainer);
        sch.setSlot(slot);
        sch.setMember(member);
        sch.setStatus(ScheduleStatus.CONFIRMED);
        return scheduleRepo.save(sch);
    }

    @Override
    @Transactional // Đảm bảo tất cả slot đều thành công, hoặc không slot nào
    public void bookPrivateSessions(Long trainerId, Long memberId, List<SlotRequest> slots) {
        UserEntity trainer = userRepo.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Huấn luyện viên"));
        UserEntity member = userRepo.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Người dùng"));

        // BƯỚC 1: GỘP CÁC SLOT BỊ TRÙNG
        List<SlotRequest> uniqueSlots = this.mergeSlots(slots);

        if (uniqueSlots.isEmpty()) { // Kiểm tra danh sách đã gộp
            throw new IllegalArgumentException("Vui lòng chọn ít nhất một slot hợp lệ.");
        }

        // BƯỚC 2: LẶP QUA DANH SÁCH ĐÃ GỘP
        for (SlotRequest slotRequest : uniqueSlots) { // Dùng uniqueSlots
            // Gọi hàm private để xử lý từng slot
            bookSingleSession(trainer, member, slotRequest.getDate(), slotRequest.getSlotNumber());
        }
    }

    private List<SlotRequest> mergeSlots(List<SlotRequest> slots) {
        if (slots == null || slots.isEmpty()) {
            return new ArrayList<>();
        }
        // Dùng LinkedHashMap để giữ thứ tự và khử trùng lặp
        var map = new java.util.LinkedHashMap<String, SlotRequest>();
        for (var s : slots) {
            if (s != null && s.getDate() != null && s.getSlotNumber() != null) {
                // Tạo key duy nhất
                String key = s.getDate() + "#" + s.getSlotNumber();
                map.putIfAbsent(key, s);
            }
        }
        return new ArrayList<>(map.values());
    }
}

