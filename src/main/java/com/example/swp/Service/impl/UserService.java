package com.example.swp.Service.impl;

import com.example.swp.DTO.RegisterDTO;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserRole;
import com.example.swp.Repository.IUserRepository;
import com.example.swp.Service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserEntity registerUser(RegisterDTO dto) {
        UserEntity checkUserName = userRepository.findByUserName(dto.getUserName()).orElse(null);
        UserEntity checkEmail = userRepository.findByEmail(dto.getEmail());
        if(checkUserName == null && checkEmail == null){
            UserEntity newUser = new UserEntity();
            newUser.setUserName(dto.getUserName());
            newUser.setEmail(dto.getEmail());
            newUser.setFirstName(dto.getFirstName());
            newUser.setLastName(dto.getLastName());
            newUser.setRole(UserRole.MEMBER);
            newUser.setPassword(passwordEncoder.encode(dto.getPassWord()));
            return userRepository.save(newUser);
        }
        return null;
    }
}
