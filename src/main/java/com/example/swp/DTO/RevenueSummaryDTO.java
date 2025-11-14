package com.example.swp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RevenueSummaryDTO {
    private Double totalRevenue;
    private Double todayRevenue;
    private Double monthRevenue;
}
