package com.example.swp.Service.impl;

import com.example.swp.DTO.EquipmentDTO;
import com.example.swp.Entity.EquipmentEntity;
import com.example.swp.Repository.EquipmentRepository;
import com.example.swp.Service.IEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


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
        equipment.setId(dto.getId());
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
        if (equipment.getStatus() == EquipmentEntity.Status.MAINTENANCE || equipment.getStatus() == EquipmentEntity.Status.AVAILABLE){
            throw new IllegalArgumentException("Chỉ được xóa thiết bị đã hỏng");
        }
        equipmentRepository.deleteById(id);
    }

    @Override
    public Optional<EquipmentEntity> getEquipmentById(Long id) {
        return equipmentRepository.findById(id);
    }

    @Override
    public Page<EquipmentEntity> getAllEquipmentPaged(Pageable pageable) {
        return equipmentRepository.findAll(pageable);
    }

    @Override
    public List<EquipmentEntity> findByName(String name) {
        if(name == null || name.trim().isEmpty()){
            return equipmentRepository.findAll();
        }
        return equipmentRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<EquipmentEntity> findByStatus(EquipmentEntity.Status status) {
        if(status == null){
            return equipmentRepository.findAll();
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
    public Page<EquipmentEntity> searchAdvancedPaged(String name, Integer quantityMin, Integer quantityMax, LocalDate startDate, LocalDate endDate, EquipmentEntity.Status status, Pageable pageable) {
        return equipmentRepository.searchAdvanced(name, quantityMin, quantityMax, startDate, endDate, status, pageable);
    }


}
