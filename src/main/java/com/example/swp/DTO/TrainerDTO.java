package com.example.swp.DTO;

import com.example.swp.Enums.UserGender;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TrainerDTO {
    private long id;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 150, message = "Email tối đa 150 ký tự")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, max = 64, message = "Mật khẩu 8-64 ký tự")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[^\\s]{8,64}$",
            message = "Mật khẩu phải có chữ, số và không có khoảng trắng")
    private String passWord;

    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 50, message = "Username 3-50 ký tự")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)(?!\\d+$)[\\p{L}\\p{N}\\-._]+$",
            message = "Username chỉ gồm chữ và số * không toàn số, không cách")
    private String userName;

    @NotBlank(message = "Họ không được để trống")
    @Size(max = 50, message = "Họ tối đa 50 ký tự")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)[\\p{L}]+(?:\\s[\\p{L}]+)*$",
            message = "Họ chỉ gồm chữ, không cách đầu/cuối")
    private String firstName;

    @NotBlank(message = "Tên không được để trống")
    @Size(max = 50, message = "Tên tối đa 50 ký tự")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)[\\p{L}]+(?:\\s[\\p{L}]+)*$",
            message = "Tên chỉ gồm chữ, không cách đầu/cuối")
    private String lastName;

    @NotBlank(message = "Chuyên môn không được để trống")
    @Size(max = 100, message = "Chuyên môn tối đa 100 ký tự")
    @Pattern(regexp = "^(?!\\s)(?!.*\\s$)(?!\\d+$)[\\p{L}\\p{N}][\\p{L}\\p{N}\\s\\-._]*$",
            message = "Chuyên môn không hợp lệ")
    private String specialization;

    @Size(max = 1000, message = "Chứng chỉ tối đa 1000 ký tự")
    private String certificationInfo;

    @NotNull(message = "Giới tính là bắt buộc")
    private UserGender gender;

    @Size(max = 500, message = "Avatar URL tối đa 500 ký tự")
    private String avatar;

    @Size(max = 2000, message = "Bio tối đa 2000 ký tự")
    private String bio;

}
