package com.example.swp.Service.impl;

import com.example.swp.DTO.TrainerDTO;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserRole;

import java.util.List;

public interface ITrainerService {
    List<UserEntity> getAllTrainer(UserRole TRAINER);
    UserEntity createTrainer(TrainerDTO dto);
    UserEntity updateTrainer(TrainerDTO dto);
    UserEntity deleteTrainer(Long id);

}
