package com.example.swp.Service;

import com.example.swp.DTO.RegisterDTO;
import com.example.swp.Entity.UserEntity;
import java.util.Optional;


public interface IUserService {
    UserEntity registerUser(RegisterDTO dto);
    UserEntity findByUserName(String userName);
    boolean existedByEmail(String email);
    UserEntity creatOrUpdateUser(UserEntity model);
    Optional<UserEntity> findByEmail(String email);
    UserEntity findByUserNameOrEmail (String input);
    UserEntity loginUser(String username, String rawPassword);
}
