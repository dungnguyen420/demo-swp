package com.example.swp.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

public class UserEntity {
    public String userName;

    public String phone;

    public String firstName;

    public UserGender gender;

    public String lastName;

    public LocalDateTime birthDate;

    public String avatar;

    public boolean isActive = true;

    public String email;

    public String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private UserRole role;

}
