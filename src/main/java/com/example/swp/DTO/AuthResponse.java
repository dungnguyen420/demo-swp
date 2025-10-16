package com.example.swp.DTO;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String userName;
}
