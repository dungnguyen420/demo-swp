package com.example.swp.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PackageDTO {
    @Pattern( regexp = "^(?!\\s)(?!.*\\s$)[\\p{L}\\p{N}]+(?:\\s[\\p{L}\\p{N}]+)*$",
            message = "Tên không được có ký tự đặc biệt")
    @Size(max = 20, message = "Tên không được vượt quá 20 ký tự")
    private String name;


    @NotBlank(message = "Mô tả không được để trống")
    @Size(min = 10, max = 255, message = "Mô tả phải từ 10 đến 255 ký tự")
    private String description;

    @NotNull(message = "Giá tiền không được để trống")
    @Positive(message = "Giá tiền phải là số dương")
    private double price;

    @NotNull(message = "Thời hạn gói không được để trống")
    @Positive(message = "Thời hạn (tháng) phải là số dương")
    private Integer durationMonth;
}
