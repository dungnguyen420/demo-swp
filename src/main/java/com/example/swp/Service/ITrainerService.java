package com.example.swp.Service;

import com.example.swp.DTO.TrainerDTO;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserGender;
import com.example.swp.Enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITrainerService {
    List<UserEntity> getAllTrainer(UserRole TRAINER);
    UserEntity createTrainer(TrainerDTO dto);
    UserEntity updateTrainer(TrainerDTO dto);
    UserEntity deleteTrainer(Long id);
    TrainerDTO trainerDetail(Long id);

    Page<UserEntity> search(String name, UserGender gender, String specialization, Pageable pageable);


}
