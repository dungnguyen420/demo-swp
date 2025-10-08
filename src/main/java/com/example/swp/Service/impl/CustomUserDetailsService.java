package com.example.swp.Service.impl;

import com.example.swp.Entity.UserEntity;
import com.example.swp.Repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrUserName) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserName(emailOrUserName)
                .or(() -> userRepository.findByEmail(emailOrUserName))
                .orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại!"));

        return new CustomUserDetails(user);
    }
}
