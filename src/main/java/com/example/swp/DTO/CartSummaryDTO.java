package com.example.swp.DTO;

import lombok.Data;

@Data
public class CartSummaryDTO {

    private java.util.List<CartItemDTO> items;
    private int totalItems;
    private double totalAmount;
}
