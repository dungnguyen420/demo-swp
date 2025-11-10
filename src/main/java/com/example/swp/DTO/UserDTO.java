package com.example.swp.DTO;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class UserDTO {
    @Id
    private Long id;

    private String username;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String avatarUrl;

    private String role;
}
