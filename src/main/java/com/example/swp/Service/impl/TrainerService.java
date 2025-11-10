package com.example.swp.Service.impl;

import com.example.swp.DTO.TrainerDTO;
import com.example.swp.DTO.TrainerUpdateDTO;
import com.example.swp.Entity.TrainerProfileEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.Status;
import com.example.swp.Enums.UserGender;
import com.example.swp.Enums.UserRole;
import com.example.swp.Repository.TrainerRepository;
import com.example.swp.Service.ITrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerService implements ITrainerService {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private TrainerRepository trainerRepository;

    public List<UserEntity> getAllTrainer(UserRole role) {
        return trainerRepository.findAllTrainerWithProfile(role);
    }


public Page<UserEntity> search(String name, UserGender gender, String specialization, Pageable pageable) {
    return trainerRepository.searchTrainer(name, gender, specialization, pageable);
}


    public UserEntity createTrainer(TrainerDTO dto) {
        if (trainerRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new RuntimeException("Email đã được sử dụng");
        }
        if (trainerRepository.findByUserName(dto.getUserName()).isPresent()){
            throw new RuntimeException("UserName đã được sử dụng");
        }
        UserEntity trainer = new UserEntity();

            trainer.setEmail(dto.getEmail().trim().replaceAll("\\s{2,}", " ") );
            trainer.setRole(UserRole.TRAINER);
            trainer.setPassword(encoder.encode(dto.getPassWord()));
            trainer.setUserName(dto.getUserName().trim().replaceAll("\\s{2,}", " ") );
            trainer.setFirstName(dto.getFirstName().trim().replaceAll("\\s{2,}", " ") );
            trainer.setLastName(dto.getLastName().trim().replaceAll("\\s{2,}", " ") );
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
    public UserEntity updateTrainer(TrainerUpdateDTO dto) {
        UserEntity existingTrainer = trainerRepository.findById(dto.getId())
                .orElseThrow(()-> new RuntimeException("không tìm thấy trainer này"));

            existingTrainer.setFirstName(dto.getFirstName().trim().replaceAll("\\s{2,}", " "));
            existingTrainer.setLastName(dto.getLastName().trim().replaceAll("\\s{2,}", " "));
            existingTrainer.setEmail(dto.getEmail().trim().replaceAll("\\s{2,}", " "));
            if (dto.getPassWord() != null && !dto.getPassWord().isEmpty()) {
                existingTrainer.setPassword(encoder.encode(dto.getPassWord().trim().replaceAll("\\s{2,}", " ")));
            }
            existingTrainer.setRole(UserRole.TRAINER);
            existingTrainer.setUserName(dto.getUserName().trim().replaceAll("\\s{2,}", " "));
            existingTrainer.setBio(dto.getBio());

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
        dtoTrainer.setBio(trainer.getBio());

        dtoTrainer.setGender(trainer.getGender());

        if (profile != null) {
            dtoTrainer.setSpecialization(profile.getSpecialization());
            dtoTrainer.setCertificationInfo(profile.getCertificationInfo());
        }

        return dtoTrainer;
    }

}
