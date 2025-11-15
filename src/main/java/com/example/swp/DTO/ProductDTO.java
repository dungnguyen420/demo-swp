package com.example.swp.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProductDTO {
    private long id;
    @NotBlank(message = "Name cannot be blank")
    @Pattern(
            regexp = "^[A-Za-z0-9 ]+$",
            message = "Name can only contain letters, numbers and spaces"
    )
    private String name;
    @NotBlank(message = "Description cannot be blank")
    @Pattern(
            regexp = "^[A-Za-z0-9 ]+$",
            message = "Description can only contain letters, numbers and spaces"
    )
    private String description;
    @NotNull(message = "Price cannot be null")
    @PositiveOrZero(message = "Price must be equal or greater than 0")
    private double price;
    @NotNull(message = "Quantity cannot be null")
    @PositiveOrZero(message = "Quantity must be equal or greater than 0")
    private int quantity;
    @NotBlank(message = "Image cannot be blank")
    private String image;
}
