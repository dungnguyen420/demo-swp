package com.example.swp.Service.impl;

import com.example.swp.DTO.EquipmentDTO;
import com.example.swp.Entity.EquipmentEntity;

import com.example.swp.Repository.EquipmentRepository;
import com.example.swp.Service.IEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipmentService implements IEquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Override
    public EquipmentEntity saveOrUpdateEquipmentEntity(EquipmentDTO dto, Long id) {
        EquipmentEntity equipment;
        if (id != null) {
            equipment = equipmentRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Thiết bị không tồn tại"));
        } else {
            equipment = new EquipmentEntity();
        }

        equipment.setName(dto.getName());
        equipment.setQuantity(dto.getQuantity());
        equipment.setPurchaseDate(dto.getPurchaseDate());
        equipment.setStatus(dto.getStatus() != null ? dto.getStatus() : EquipmentEntity.Status.AVAILABLE);
        equipment.setImage(dto.getImage());

        return equipmentRepository.save(equipment);
    }

    @Override
    public void deleteEquipmemt(Long id) {
        EquipmentEntity equipment = equipmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Thiết bị không tồn tại"));
        if (equipment.getStatus() == EquipmentEntity.Status.MAINTENANCE){
            throw new IllegalArgumentException("Không thể xóa thiết bị đang bảo trì");
        }
        equipmentRepository.deleteById(id);
    }

    @Override
    public Optional<EquipmentEntity> getEquipmentById(Long id) {
        return equipmentRepository.findById(id);
    }

    @Override
    public List<EquipmentEntity> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    @Override
    public List<EquipmentEntity> findByName(String name) {
        if(name == null || name.trim().isEmpty()){
            return getAllEquipment();
        }
        return equipmentRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<EquipmentEntity> findByStatus(EquipmentEntity.Status status) {
        if(status == null){
            return getAllEquipment();
        }
        return equipmentRepository.findByStatus(status);
    }

    @Override
    public EquipmentDTO getEquipmentDTOById(Long id) {
        EquipmentEntity entity = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Thiết bị không tìm thấy"));
        EquipmentDTO dto = new EquipmentDTO();
        dto.setName(entity.getName());
        dto.setQuantity(entity.getQuantity());
        dto.setPurchaseDate(entity.getPurchaseDate());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    @Override
    public List<EquipmentEntity> searchAdvanced(String name, Integer quantity, LocalDate purchaseDate, EquipmentEntity.Status status) {
        List<EquipmentEntity> all = equipmentRepository.findAll();

        return all.stream()
                .filter(e -> name == null || name.trim().isEmpty() ||
                        e.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(e -> quantity == null || e.getQuantity() == quantity)
                .filter(e -> purchaseDate == null || e.getPurchaseDate() != null &&
                        e.getPurchaseDate().equals(purchaseDate))
                .filter(e -> status == null || e.getStatus() == status)
                .toList();
    }

}
