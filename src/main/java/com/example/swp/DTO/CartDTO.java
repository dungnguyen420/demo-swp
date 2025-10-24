package com.example.swp.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class CartDTO {
    private List<CartItemDTO> items = new ArrayList<>();
    private double totalPrice = 0.0;
     private Long userId;
     private String userEmail;
}
