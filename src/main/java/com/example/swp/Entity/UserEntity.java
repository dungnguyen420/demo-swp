package com.example.swp.Entity;

import com.example.swp.Enums.Status;
import com.example.swp.Enums.UserGender;
import com.example.swp.Enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
@Data
@Entity(name="Users")
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    // Quan hệ 1-1 với TrainerProfile
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private TrainerProfileEntity trainerProfile;

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
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

}
