package com.example.swp.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlotRequest {
    @NotNull(message = "Ngày bắt buộc")
    private LocalDate date;

    @NotNull(message = "Slot bắt buộc")
    @Min(1) @Max(6)
    private Integer slotNumber;
}