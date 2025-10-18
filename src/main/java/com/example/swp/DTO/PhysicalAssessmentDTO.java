package com.example.swp.DTO;

import java.time.LocalDate;

public class PhysicalAssessmentDTO {
    public Long id;
    public Long memberId;
    public Long trainerId;
    public LocalDate assessmentDate;
    public Double weight;
    public Double bodyFat;
    public Double muscleMass;
    public String tests;
    public String notes;
}