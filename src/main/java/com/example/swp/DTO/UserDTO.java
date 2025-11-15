package com.example.swp.DTO;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class UserDTO {
    @Id
    private Long id;

    @NotEmpty(message = "Tên đăng nhập không được để trống")
    @Size(min = 5, max = 20, message = "Tên đăng nhập phải từ 5 đến 20 ký tự")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Tên đăng nhập chỉ chứa chữ (không dấu), số và gạch dưới (_)")
    private String userName;

    @NotEmpty(message = "Họ không được để trống")
    @Size(max = 20, message = "Họ không được vượt quá 20 ký tự")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)[\\p{L}]+(?:\\s[\\p{L}]+)*$",
            message = "Tên không được có ký tự đặc biệt")
    private String firstName;

    @NotEmpty(message = "Tên không được để trống")
    @Size(max = 20, message = "Tên không được vượt quá 20 ký tự")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)[\\p{L}]+(?:\\s[\\p{L}]+)*$",
            message = "Tên không được có ký tự đặc biệt")
    private String lastName;


    @NotEmpty(message = "Email không được để trống")
    @Email(message = "Định dạng email không hợp lệ")
    private String email;

    private String phoneNumber;

    private String avatarUrl;

    private String role;
}
