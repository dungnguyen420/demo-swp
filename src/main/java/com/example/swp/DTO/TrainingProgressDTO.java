package com.example.swp.DTO;

import java.time.LocalDateTime;

public class TrainingProgressDTO {
    public Long id;
    public Long memberId;
    public LocalDateTime sessionDate;
    public String notes;
    public Double weight;
    public Double bodyFat;
    public String exercisesSummary;
    public String status;
    public String imageUrl;
}