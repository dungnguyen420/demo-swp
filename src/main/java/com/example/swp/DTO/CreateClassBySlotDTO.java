package com.example.swp.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateClassBySlotDTO {
    private Long trainerUserId;             // để trống nếu actor là TRAINER
    private String name;
    private String description;
    private Integer capacity;
    // Danh sách ngày + slotNumber muốn tạo (ví dụ: 2025-10-14, slot 2)
    private List<LocalDate> dates;
    private List<Integer> slotNumbers;
    private List<SlotRequest> slots; // phải có field này
// mỗi date ghép với 1 slot hoặc nhiều (tùy bạn)
}
