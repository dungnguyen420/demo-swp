package com.example.swp.Service;

import com.example.swp.Entity.UserEntity;
import com.example.swp.Repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
