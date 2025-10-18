package com.example.swp.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "progress")
public class ProgressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;      // ID người tập
    private String activity;    // "Chạy bộ", "Đẩy tạ"...
    private Double duration;    // phút
    private Double calories;    // kcal
    private LocalDateTime date; // ngày cập nhật
}
