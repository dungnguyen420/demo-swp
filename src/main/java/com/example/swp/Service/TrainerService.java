package com.example.swp.Service;

import com.example.swp.DTO.RegisterDTO;
import com.example.swp.DTO.TrainerDTO;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserRole;
import com.example.swp.Repository.TrainerRepository;
import com.example.swp.Service.impl.ITrainerService;
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
        trainer.setRole(UserRole.TRAINER);
        trainer.setPassword(encoder.encode(trainer.getPassword()));
        trainer.setUserName(dto.getUserName());
        trainer.setFirstName(dto.getFirstName());
        trainer.setLastName(dto.getLastName());
        trainerRepository.save(trainer);
        return trainer;
    }

    @Override
    public UserEntity updateTrainer(TrainerDTO dto) {
        UserEntity existingTrainer = trainerRepository.findById(dto.getId()).get();
        if(existingTrainer != null){
            existingTrainer.setFirstName(dto.getFirstName());
            existingTrainer.setLastName(dto.getLastName());
            existingTrainer.setEmail(dto.getEmail());
            if (dto.getPassWord() != null && !dto.getPassWord().isEmpty()) {
                existingTrainer.setPassword(encoder.encode(dto.getPassWord()));
            }
            existingTrainer.setRole(UserRole.TRAINER);
            existingTrainer.setUserName(dto.getUserName());
            UserEntity updatedTrainer = trainerRepository.save(existingTrainer);
            return updatedTrainer;
        } else {
        throw new RuntimeException("không tìm thấy trainer này");
        }
    }

    @Override
    public UserEntity deleteTrainer(Long id) {
        UserEntity existingTrainer = trainerRepository.findById(id).get();
        if (existingTrainer != null) {
            trainerRepository.delete(existingTrainer);
            return existingTrainer;
        } else {
            throw new RuntimeException("không tìm thấy trainer này");

        }
        }

}
