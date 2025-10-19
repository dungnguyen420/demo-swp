package com.example.swp.DTO;

import lombok.Data;

@Data
public class CartItemDTO {

    private Long productId;
    private String productName;
    private double unitPrice;
    private int quantity;
    private double lineTotal;
    private String image;
}
