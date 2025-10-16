package com.example.swp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlotRequest {
    private LocalDate date;
    private Integer slotNumber; // 1..6
}