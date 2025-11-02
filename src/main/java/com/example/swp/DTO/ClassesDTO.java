package com.example.swp.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ClassesDTO {
    @NotNull(message = "Trainer bắt buộc")
    private Long trainerId;

    @NotBlank(message = "Tên lớp không được để trống")
    @Size(min = 2, max = 100, message = "Tên lớp từ 2-100 ký tự")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)(?!\\d+$)[\\p{L}\\p{N}][\\p{L}\\p{N}\\s\\-._]*$",
            message = "Tên lớp không hợp lệ")
    private String name;

    @Size(max = 2000, message = "Mô tả tối đa 2000 ký tự")
    private String description;

    @NotEmpty(message = "Lịch học không được rỗng")
    private List<LocalDateTime> scheduleTimes;

    @NotNull(message = "Sức chứa bắt buộc")
    @Min(value = 1, message = "Sức chứa tối thiểu 1")
    @Max(value = 8, message = "Sức chứa tối đa 8")
    private Integer capacity;

    @NotNull(message = "Thời lượng bắt buộc")
    @Min(value = 15, message = "Thời lượng tối thiểu 15 phút")
    @Max(value = 240, message = "Thời lượng tối đa 240 phút")
    private Integer durationMinutes;
}
