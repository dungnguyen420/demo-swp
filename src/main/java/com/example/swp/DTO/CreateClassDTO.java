package com.example.swp.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateClassDTO {
    private Long trainerId;
    private String name;
    private String description;
    private Integer capacity;
    private List<LocalDateTime> scheduleTimes;
}
