package com.example.swp.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterDTO {
    private String userName;
    private String passWord;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}
