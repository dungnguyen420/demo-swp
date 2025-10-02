package com.example.swp.Enums;

public enum DayOfWeekEnum {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    private final int value;

    DayOfWeekEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DayOfWeekEnum fromValue(int value) {
        for (DayOfWeekEnum d : values()) {
            if (d.value == value) return d;
        }
        throw new IllegalArgumentException("Invalid day_of_week: " + value);
    }
}

