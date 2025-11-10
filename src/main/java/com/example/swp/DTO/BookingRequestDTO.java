package com.example.swp.DTO;

import lombok.Data;

import java.util.List;

@Data
public class BookingRequestDTO {
    private List<SlotRequest> slots;
}
