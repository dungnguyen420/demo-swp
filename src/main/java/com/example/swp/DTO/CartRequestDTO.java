package com.example.swp.DTO;

import lombok.Data;

@Data
public class CartRequestDTO {
    private Long productId;
    private Integer quantity;
}
