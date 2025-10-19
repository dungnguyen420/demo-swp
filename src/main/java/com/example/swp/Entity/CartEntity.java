package com.example.swp.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "carts")
public class CartEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Version
    @Column(name = "version")
    private Integer version;

    public CartEntity() {}

    public CartEntity(UserEntity user) {
        this.user = user;
        this.userId = user.getId();
    }


}


