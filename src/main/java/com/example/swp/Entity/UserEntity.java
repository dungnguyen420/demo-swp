package com.example.swp.Entity;

import com.example.swp.Enums.Status;
import com.example.swp.Enums.UserGender;
import com.example.swp.Enums.UserRole;
import io.jsonwebtoken.lang.Classes;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name="Users")
public class UserEntity extends BaseEntity {


    private String userName;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private TrainerProfileEntity trainerProfile;

    private String phone;

    private String firstName;

    @Enumerated(EnumType.STRING)
    private UserGender gender;

    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime birthDate;

    private String avatar;

    private boolean isActive = true;

    private String email;

    private String password;


    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private UserRole role;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    private String bio;

}
