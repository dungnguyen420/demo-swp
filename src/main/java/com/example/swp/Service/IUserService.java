package com.example.swp.Service;

import com.example.swp.DTO.RegisterDTO;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserGender;
import com.example.swp.Enums.UserRole;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface IUserService {
    UserEntity registerUser(RegisterDTO dto);
    UserEntity findByUserName(String userName);
    boolean existedByEmail(String email);
    UserEntity creatOrUpdateUser(UserEntity model);
    Optional<UserEntity> findByEmail(String email);
    UserEntity findByUserNameOrEmail (String input);
    UserEntity loginUser(String usernameOrEmail, String rawPassword);
    boolean authenticateUser(String usernameOrEmail, String password, HttpSession session);
    List<UserEntity> findByRole(UserRole member);
    void deleteUser(Long Id);
    UserEntity updateUser(Long id, RegisterDTO dto);
    UserEntity findById(Long Id);
    Page<UserEntity> findByRole(UserRole role, Pageable pageable);
    Page<UserEntity> searchUsers(String keyword, Pageable pageable);
    UserEntity updateProfile(String userName, UserEntity formUser);
}
