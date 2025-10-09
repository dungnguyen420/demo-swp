package com.example.swp.Service.impl;

import com.example.swp.DTO.TrainerDTO;
import com.example.swp.Entity.TrainerProfileEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.Status;
import com.example.swp.Enums.UserRole;
import com.example.swp.Repository.TrainerRepository;
import com.example.swp.Service.ITrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService implements ITrainerService {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private TrainerRepository trainerRepository;

    @Override
    public List<UserEntity> getAllTrainer(UserRole trainer) {
        return trainerRepository.findByRole(UserRole.TRAINER);
    }


    public UserEntity createTrainer(TrainerDTO dto) {
        if (trainerRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new RuntimeException("Email đã được sử dụng");
        }
        UserEntity trainer = new UserEntity();

            trainer.setEmail(dto.getEmail());
            trainer.setRole(UserRole.TRAINER);
            trainer.setPassword(encoder.encode(dto.getPassWord()));
            trainer.setUserName(dto.getUserName());
            trainer.setFirstName(dto.getFirstName());
            trainer.setLastName(dto.getLastName());
            trainer.setGender(dto.getGender());
            trainer.setStatus(Status.ACTIVE);
            trainer.setActive(true);

        TrainerProfileEntity profile= new TrainerProfileEntity();

            profile.setSpecialization(dto.getSpecialization());
            profile.setCertificationInfo(dto.getCertificationInfo());

        profile.setUser(trainer);
        trainer.setTrainerProfile(profile);
        trainerRepository.save(trainer);
        return trainer;
    }

    @Override
    public UserEntity updateTrainer(TrainerDTO dto) {
        UserEntity existingTrainer = trainerRepository.findById(dto.getId())
                .orElseThrow(()-> new RuntimeException("không tìm thấy trainer này"));

            existingTrainer.setFirstName(dto.getFirstName());
            existingTrainer.setLastName(dto.getLastName());
            existingTrainer.setEmail(dto.getEmail());
            if (dto.getPassWord() != null && !dto.getPassWord().isEmpty()) {
                existingTrainer.setPassword(encoder.encode(dto.getPassWord()));
            }
            existingTrainer.setRole(UserRole.TRAINER);
            existingTrainer.setUserName(dto.getUserName());

        TrainerProfileEntity profile = existingTrainer.getTrainerProfile();
        if (profile == null) {
            profile = new TrainerProfileEntity();
            profile.setUser(existingTrainer);
            existingTrainer.setTrainerProfile(profile);
        }
            profile.setSpecialization(dto.getSpecialization());
            profile.setCertificationInfo(dto.getCertificationInfo());


        UserEntity updatedTrainer = trainerRepository.save(existingTrainer);
            return updatedTrainer;
        }


    @Override
    public UserEntity deleteTrainer(Long id) {
        UserEntity existingTrainer = trainerRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("không tìm thấy trainer này"));

            trainerRepository.delete(existingTrainer);
            return existingTrainer;
    }

    @Override
    public TrainerDTO trainerDetail(Long id) {
        UserEntity trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trainer với ID: " + id));

        TrainerProfileEntity profile = trainer.getTrainerProfile();
        TrainerDTO dtoTrainer = new TrainerDTO();

        dtoTrainer.setId(trainer.getId());
        dtoTrainer.setFirstName(trainer.getFirstName());
        dtoTrainer.setLastName(trainer.getLastName());
        dtoTrainer.setEmail(trainer.getEmail());
        dtoTrainer.setUserName(trainer.getUserName());
        dtoTrainer.setAvatar(trainer.getAvatar());

        dtoTrainer.setGender(trainer.getGender());

        if (profile != null) {
            dtoTrainer.setSpecialization(profile.getSpecialization());
            dtoTrainer.setCertificationInfo(profile.getCertificationInfo());
        }

        return dtoTrainer;
    }



}
