package com.example.swp.Service.impl;

import com.example.swp.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {

    private final UserEntity user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {// Nếu có phân quyền thì thêm role vào đây
        return Collections.singletonList(
                new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" +user.getRole().name())
        );
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // hoặc getEncodedPassword nếu bạn mã hóa
    }

    @Override
    public String getUsername() {
        return user.getUserName(); // hoặc getEmail() tùy logic của bạn
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Có thể kiểm tra trạng thái nếu có
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Có thể kiểm tra trạng thái nếu có
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Có thể kiểm tra trạng thái nếu có
    }

    @Override
    public boolean isEnabled() {
        return true; // Có thể kiểm tra trạng thái nếu có
    }
}