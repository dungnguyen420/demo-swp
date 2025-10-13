package com.example.swp.Enums;

import java.time.LocalTime;

public enum SlotNumber {
    SLOT_1(1, LocalTime.of(6, 0),  LocalTime.of(8, 0)),
    SLOT_2(2, LocalTime.of(8, 0),  LocalTime.of(10, 0)),
    SLOT_3(3, LocalTime.of(10,0),  LocalTime.of(12, 0)),
    SLOT_4(4, LocalTime.of(12,0),  LocalTime.of(14, 0)),
    SLOT_5(5, LocalTime.of(14,0),  LocalTime.of(16, 0)),
    SLOT_6(6, LocalTime.of(16,0),  LocalTime.of(18, 0));

    public final int number;
    public final LocalTime start;
    public final LocalTime end;

    SlotNumber(int number, LocalTime start, LocalTime end) {
        this.number = number;
        this.start = start;
        this.end = end;
    }

    public static SlotNumber fromNumber(int n) {
        for (var s : values()) if (s.number == n) return s;
        throw new IllegalArgumentException("Slot phải từ 1..6");
    }

    public static SlotNumber fromTime(LocalTime t) {
        if (t.isBefore(LocalTime.of(6,0)) || !t.isBefore(LocalTime.of(18,0)))
            throw new IllegalArgumentException("Chỉ hỗ trợ 06:00–18:00");
        for (var s : values()) {
            if (!t.isBefore(s.start) && t.isBefore(s.end)) return s;
        }
        // Nếu t nằm đúng mốc 08:00,10:00... cũng match ở vòng lặp trên (vì !isBefore)
        throw new IllegalArgumentException("Giờ không nằm trong slot cố định");
    }
}
