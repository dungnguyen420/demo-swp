package com.example.swp.DTO;

import com.example.swp.Enums.UserGender;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TrainerUpdateDTO {
    @NotNull
    private Long id;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @Size(min = 8, max = 64, message = "Mật khẩu 8-64 ký tự")
    @Pattern(regexp = "^(|(?=.*[A-Za-z])(?=.*\\d)[^\\s]{8,64})$",
            message = "Nếu nhập, mật khẩu phải có chữ, số và không có khoảng trắng")
    private String passWord;

    @NotBlank @Size(min = 3, max = 50)
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)(?!\\d+$)[\\p{L}\\p{N}\\-._]+$")
    private String userName;

    @NotBlank @Size(max = 50)
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)[\\p{L}]+(?:\\s[\\p{L}]+)*$")
    private String firstName;

    @NotBlank @Size(max = 50)
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)[\\p{L}]+(?:\\s[\\p{L}]+)*$")
    private String lastName;

    @NotBlank @Size(max = 100)
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)(?!\\d+$)[\\p{L}\\p{N}][\\p{L}\\p{N}\\s\\-._]*$")
    private String specialization;

    @Size(max = 1000)
    private String certificationInfo;

    @NotNull
    private UserGender gender;

    @Size(max = 500)
    private String avatar;

    @Size(max = 2000)
    private String bio;
}
