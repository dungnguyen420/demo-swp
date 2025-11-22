package com.example.swp.Service;

import com.example.swp.DTO.EquipmentDTO;
import com.example.swp.Entity.EquipmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IEquipmentService {
    EquipmentEntity saveOrUpdateEquipmentEntity(EquipmentDTO dto, Long id);

    void deleteEquipmemt (Long id);

    Optional<EquipmentEntity> getEquipmentById(Long id);

    Page<EquipmentEntity> getAllEquipmentPaged(Pageable pageable);

    List<EquipmentEntity> findByName(String name);

    List<EquipmentEntity> findByStatus(EquipmentEntity.Status status);

    EquipmentDTO getEquipmentDTOById(Long id);

    Page<EquipmentEntity> searchAdvancedPaged(
            String name,
            Integer quantityMin,
            Integer quantityMax,
            LocalDate startDate,
            LocalDate endDate,
            EquipmentEntity.Status status,
            Pageable pageable
    );
}

