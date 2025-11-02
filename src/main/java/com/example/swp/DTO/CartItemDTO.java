package com.example.swp.DTO;

import lombok.Data;

@Data
public class CartItemDTO {
    private String entityType;
    private Long cartItemId;
    private Long packageId;
    private Long packageName;
    private String itemName;
    private Long productId;
    private String productName;
    private double unitPrice;
    private int quantity;
    private double lineTotal;
    private String image;
}
