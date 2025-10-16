package com.example.swp.DTO;

import lombok.Data;

@Data
public class AuthRequest {
    private String emailOrUserName;
    private String passWord;
}
