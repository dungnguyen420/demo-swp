package com.example.swp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyRevenueDTO {
    private String dayLabel;
    private Double revenue;
}
