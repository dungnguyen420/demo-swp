package com.example.swp.Service;

import com.example.swp.Entity.UserEntity;
import com.example.swp.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        UserEntity user = userRepository.loadUserByUserName(username).get();
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return new User(user.getUserName(), user.getPassword(), new ArrayList<>());
    }
}
