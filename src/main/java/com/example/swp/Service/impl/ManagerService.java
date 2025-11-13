package com.example.swp.Service.impl;

import com.example.swp.DTO.ManagerDTO;
import com.example.swp.DTO.ManagerUpdateDTO;
import com.example.swp.DTO.TrainerDTO;
import com.example.swp.DTO.TrainerUpdateDTO;
import com.example.swp.Entity.TrainerProfileEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.Status;
import com.example.swp.Enums.UserGender;
import com.example.swp.Enums.UserRole;
import com.example.swp.Repository.ManagerRepository;
import com.example.swp.Service.IManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService implements IManagerService {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ManagerRepository managerRepository;



    @Override
    public Page<UserEntity> searchManagers(String name, UserGender gender, Pageable pageable) {
        String searchName = (name != null && !name.trim().isEmpty()) ? name.trim() : null;

        return managerRepository.searchManagers(searchName, gender, pageable);
    }

    public UserEntity createManager(ManagerDTO dto) {
        if (managerRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new RuntimeException("Email đã được sử dụng");
        }
        if (managerRepository.findByUserName(dto.getUserName()).isPresent()){
            throw new RuntimeException("UserName đã được sử dụng");
        }
        UserEntity manager = new UserEntity();

        manager.setEmail(dto.getEmail().trim().replaceAll("\\s{2,}", " ") );
        manager.setRole(UserRole.MANAGER);
        manager.setPassword(encoder.encode(dto.getPassWord()));
        manager.setUserName(dto.getUserName().trim().replaceAll("\\s{2,}", " ") );
        manager.setFirstName(dto.getFirstName().trim().replaceAll("\\s{2,}", " ") );
        manager.setLastName(dto.getLastName().trim().replaceAll("\\s{2,}", " ") );
        manager.setGender(dto.getGender());
        manager.setStatus(Status.ACTIVE);
        manager.setActive(true);

        managerRepository.save(manager);
        return manager;
    }

    @Override
    public UserEntity updateManager(ManagerUpdateDTO dto) {
        UserEntity existingManager = managerRepository.findById(dto.getId())
                .orElseThrow(()-> new RuntimeException("không tìm thấy MANAGER này"));

        existingManager.setFirstName(dto.getFirstName().trim().replaceAll("\\s{2,}", " "));
        existingManager.setLastName(dto.getLastName().trim().replaceAll("\\s{2,}", " "));
        existingManager.setEmail(dto.getEmail().trim().replaceAll("\\s{2,}", " "));
        if (dto.getPassWord() != null && !dto.getPassWord().isEmpty()) {
            existingManager.setPassword(encoder.encode(dto.getPassWord().trim().replaceAll("\\s{2,}", " ")));
        }
        existingManager.setRole(UserRole.MANAGER);
        existingManager.setUserName(dto.getUserName().trim().replaceAll("\\s{2,}", " "));
        existingManager.setBio(dto.getBio());

        UserEntity updatedTrainer = managerRepository.save(existingManager);
        return updatedTrainer;
    }

    @Override
    public UserEntity deleteManager(Long id) {
        UserEntity existingManager = managerRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("không tìm thấy manager này"));

        managerRepository.delete(existingManager);
        return existingManager;
    }

    @Override
    public ManagerDTO managerDetail(Long id) {
        UserEntity trainer = managerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trainer với ID: " + id));

        ManagerDTO dtoManager = new ManagerDTO();

        dtoManager.setId(trainer.getId());
        dtoManager.setFirstName(trainer.getFirstName());
        dtoManager.setLastName(trainer.getLastName());
        dtoManager.setEmail(trainer.getEmail());
        dtoManager.setUserName(trainer.getUserName());
        dtoManager.setAvatar(trainer.getAvatar());
        dtoManager.setBio(trainer.getBio());
        dtoManager.setGender(trainer.getGender());



        return dtoManager;
    }


}
