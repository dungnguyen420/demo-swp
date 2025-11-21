package com.example.swp.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProductDTO {
    private long id;
    @NotBlank(message = "Tên không được để trống")
    @Pattern(
            regexp = "^(?!\\s)(?!.*\\s$)[\\p{L}]+(?:\\s[\\p{L}]+)*$",
            message = "Tên chỉ gồm chữ, không cách đầu/cuối"
    )
    private String name;
    @NotBlank(message = "Mô tả không được để trống")
    @Pattern(
            regexp = "^(?!\\s)(?!.*\\s$)[\\p{L}]+(?:\\s[\\p{L}]+)*$",
            message = "Mô tả chỉ gồm chữ, không cách đầu/cuối"
    )
    private String description;
    @NotNull(message = "Giá không không để trống")
    @PositiveOrZero(message = "Giá lớn hơn hoặc bằng 0")
    private double price;
    @NotNull(message = "Số lượng không để trống")
    @PositiveOrZero(message = "Số lượng lớn hơn hoặc bằng 0")
    private int quantity;
    private String image;
}
