package com.example.swp.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProductDTO {
    private long id;
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Pattern(
            regexp = "^[A-Za-z0-9]+$",
            message = "Tên sản phẩm chỉ gồm ký tự, số"
    )
    private String name;
    @NotBlank(message = "Mô tả không được để trống")
    @Pattern(
            regexp = "^[A-Za-z0-9 ]+$",
            message = "Mô tả chỉ gồm ký tự, số"
    )
    private String description;
    @NotNull(message = "Giá không được để trống")
    @PositiveOrZero(message = "Giá phải lớn hơn hoặc bằng 0")
    private double price;
    @NotNull(message = "Số lượng không được để trống")
    @PositiveOrZero(message = "Số lượng phải lớn hơn hoặc bằng 0")
    private int quantity;
    @NotBlank(message = "Ảnh không được để trống")
    private String image;
}
