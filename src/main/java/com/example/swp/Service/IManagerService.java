package com.example.swp.Service;

import com.example.swp.DTO.ManagerDTO;
import com.example.swp.DTO.ManagerUpdateDTO;
import com.example.swp.DTO.TrainerDTO;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserGender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IManagerService {

    UserEntity createManager(ManagerDTO dto);
    UserEntity updateManager(ManagerUpdateDTO dto);
    UserEntity deleteManager(Long id);
    ManagerDTO managerDetail(Long id);
    Page<UserEntity> searchManagers(String name, UserGender gender, Pageable pageable);

}
