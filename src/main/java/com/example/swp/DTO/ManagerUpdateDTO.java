package com.example.swp.DTO;

import com.example.swp.Enums.UserGender;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ManagerUpdateDTO {
    @NotNull
    private Long id;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @Pattern(regexp = "^(|(?=.*[A-Za-z])(?=.*\\d)[^\\s]{8,64})$",
            message = "Nếu nhập, mật khẩu phải có chữ, số (8-64 ký tự), không khoảng trắng")
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

    @NotNull
    private UserGender gender;

    @Size(max = 500)
    private String avatar;

    @Size(max = 2000)
    private String bio;
}
