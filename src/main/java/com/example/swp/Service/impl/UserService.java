package com.example.swp.Service.impl;

import com.example.swp.DTO.RegisterDTO;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.Status;
import com.example.swp.Enums.UserRole;
import com.example.swp.Repository.IUserRepository;
import com.example.swp.Service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntity registerUser(RegisterDTO dto) {
        UserEntity checkUserName = userRepository.findByUserName(dto.getUserName()).orElse(null);
        Optional<UserEntity> checkEmail = userRepository.findByEmail(dto.getEmail());
        if(checkUserName == null && checkEmail.isEmpty()){
            UserEntity newUser = new UserEntity();
            newUser.setUserName(dto.getUserName());
            newUser.setEmail(dto.getEmail());
            newUser.setStatus(Status.ACTIVE);
            newUser.setRole(UserRole.MEMBER);
            newUser.setFirstName(dto.getFirstName());
            newUser.setLastName(dto.getLastName());
            newUser.setPassword(passwordEncoder.encode(dto.getPassWord()));
            return userRepository.save(newUser);
        }
        return null;
    }

    @Override
    public UserEntity findByUserName(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }

    @Override
    public boolean existedByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserEntity creatOrUpdateUser(UserEntity model) {
        return userRepository.save(model);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public UserEntity findByUserNameOrEmail(String input) {
        return userRepository.findByUserName(input)
                .or(() -> userRepository.findByEmail(input))
                .orElse(null);
    }
}
