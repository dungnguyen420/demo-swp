package com.example.swp.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class EditClassDTO {
    @NotBlank(message = "Tên lớp không được để trống")
    @Size(min = 2, max = 100, message = "Tên lớp từ 2-100 ký tự")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)(?!\\d+$)[\\p{L}\\p{N}][\\p{L}\\p{N}\\s\\-._]*$",
            message = "Tên lớp không hợp lệ")
    private String name;

    @Size(max = 2000, message = "Mô tả tối đa 2000 ký tự")
    private String description;

    @NotNull(message = "Sức chứa bắt buộc")
    @Min(value = 1, message = "Sức chứa tối thiểu 1")
    @Max(value = 8, message = "Sức chứa tối đa 8")
    private Integer capacity;
    private List<SlotRequest> newSlots = new ArrayList<>();
}
