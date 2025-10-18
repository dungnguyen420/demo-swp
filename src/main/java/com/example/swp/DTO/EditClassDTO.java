package com.example.swp.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EditClassDTO {
    private String name;
    private String description;
    private Integer capacity;
    private List<SlotRequest> newSlots = new ArrayList<>();
}
