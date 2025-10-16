package com.example.swp.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ClassesDTO {
    private Long trainerId;
    private String name;
    private String description;
    private List<LocalDateTime> scheduleTimes;
    private Integer capacity;
    private Integer durationMinutes;
}
