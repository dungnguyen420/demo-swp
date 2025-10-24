package com.example.swp.DTO;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long packageId;
    private String productName;
    private int quantity;
    private double price;
}
