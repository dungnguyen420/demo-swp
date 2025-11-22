package com.example.swp.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductDTO {
    private long id;
    @NotBlank(message = "Tên không được để trống")
    @Pattern(
            regexp = "^(?!\\s)(?!.*\\s$)[\\p{L}0-9]+(?:\\s[\\p{L}0-9]+)*$",
            message = "Không có khoảng trắng ở đầu và cuối"
    )
    private String name;
    @NotBlank(message = "Mô tả không được để trống")
    @Pattern(
            regexp = "^(?!\\s)(?!.*\\s$)[\\p{L}0-9]+(?:\\s[\\p{L}0-9]+)*$",
            message = "Không có khoảng trắng ở đầu và cuối"
    )
    private String description;
    @NotNull(message = "Giá không không để trống")
    @Positive(message = "Giá lớn hơn 0")
    private double price;
    @NotNull(message = "Số lượng không để trống")
    @Positive(message = "Số lượng lớn hơn 0")
    private int quantity;
    private String image;
}
