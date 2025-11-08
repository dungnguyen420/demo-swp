package com.example.swp.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {

    @NotBlank(message = "Username không được để trống")
    @Size(min = 4, max = 20, message = "Username 4-20 ký tự")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)(?!\\d+$)[\\p{L}\\p{N}\\-._]+$",
            message = "Username chỉ gồm chữ và số")
    private String userName;

    @NotBlank(message = "password không được để trống")
    @Size(min = 6, message = "Password phải chứa ít nhất 6 ký tự")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[^\\s]{8,64}$",
            message = "Mật khẩu phải có chữ, số và không có khoảng trắng")
    private String passWord;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "First name cannot be empty")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)[\\p{L}]+(?:\\s[\\p{L}]+)*$",
            message = "Họ chỉ gồm chữ, không cách đầu/cuối")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)[\\p{L}]+(?:\\s[\\p{L}]+)*$",
            message = "Tên chỉ gồm chữ, không cách đầu/cuối")
    private String lastName;

    private String role;

}
