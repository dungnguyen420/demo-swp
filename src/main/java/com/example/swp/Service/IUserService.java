package com.example.swp.Service;

import com.example.swp.DTO.RegisterDTO;
import com.example.swp.Entity.UserEntity;

public interface IUserService {
    UserEntity registerUser(RegisterDTO dto);
}
