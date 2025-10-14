package com.example.swp.DTO;

import java.time.LocalDate;

public class NutritionPlanDTO {
    public Long id;
    public Long memberId;
    public Long trainerId;
    public LocalDate startDate;
    public LocalDate endDate;
    public Integer dailyCalories;
    public Integer proteinGrams;
    public Integer fatGrams;
    public Integer carbGrams;
    public String mealPlan;
}