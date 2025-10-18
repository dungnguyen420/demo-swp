package com.example.swp.Service.impl;

import com.example.swp.Entity.ScheduleEntity;
import com.example.swp.Entity.TimeSlotEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.ScheduleStatus;
import com.example.swp.Enums.Shift;
import com.example.swp.Enums.SlotNumber;
import com.example.swp.Repository.ScheduleRepository;
import com.example.swp.Repository.TimeSlotRepository;
import com.example.swp.Service.IScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ScheduleService implements IScheduleService {

    private final TimeSlotRepository slotRepo;
    private final ScheduleRepository scheduleRepo;

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
}

