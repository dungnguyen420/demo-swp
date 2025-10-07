package com.example.swp.Service;

import com.example.swp.DTO.RegisterDTO;
import com.example.swp.Entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface IUserService {
    UserEntity registerUser(RegisterDTO dto);
    UserEntity findByUserName(String userName);
    boolean existedByEmail(String email);
    UserEntity creatOrUpdateUser(UserEntity model);
    Optional<UserEntity> findByEmail(String email);
    UserEntity findByUserNameOrEmail (String input);
}
