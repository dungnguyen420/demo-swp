package com.example.swp.DTO;

import lombok.Data;

@Data
public class ProductDTO {
    private long id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String image;
}
