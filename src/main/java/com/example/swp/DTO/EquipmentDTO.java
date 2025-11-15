package com.example.swp.DTO;

import com.example.swp.Entity.EquipmentEntity;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class EquipmentDTO {
    private Long id;
    @NotBlank(message = "Tên thiết bị không được để trống")
    @Pattern(
            regexp = "^(?!\\s)(?!.*\\s$)(?!\\d+$)[\\p{L}\\p{N}\\-._]+$",
            message = "Tên chỉ được chứa chữ cái, số và khoảng trắng (không có ký tự đặc biệt)"
    )
    private String name;
    @NotNull(message = "Số lượng thiết bị không được để trống")
    @Positive(message = "Price must be equal or greater than 0")
    private Integer quantity;
    @NotNull(message = "Ngày nhập không được để trống")
    @PastOrPresent(message = "Ngày nhập phải là trong quá khứ hoặc hiện tại")
    private LocalDate purchaseDate;
    @NotNull(message = "Trạng thái không được để trống")
    private EquipmentEntity.Status status;
    @NotBlank(message = "Ảnh không được để trống")
    private String image;

}
