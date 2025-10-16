package com.example.swp.Service.impl;

import com.example.swp.Entity.EquipmentEntity;
import com.example.swp.Repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    // Tạo mới thiết bị
    public EquipmentEntity createEquipment(EquipmentEntity equipment) {
        return equipmentRepository.save(equipment);
    }

    // Lấy danh sách toàn bộ thiết bị
    public List<EquipmentEntity> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    // Lấy thiết bị theo ID
    public EquipmentEntity getEquipmentById(Long id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
    }

    // Cập nhật thông tin thiết bị
    public EquipmentEntity updateEquipment(Long id, EquipmentEntity updated) {
        EquipmentEntity existing = getEquipmentById(id);

        existing.setName(updated.getName());
        existing.setLocation(updated.getLocation());
        existing.setPurchaseDate(updated.getPurchaseDate());
        existing.setStatus(updated.getStatus());

        return equipmentRepository.save(existing);
    }

    // Xóa thiết bị
    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }

    // Cập nhật trạng thái riêng (ví dụ: Trainer báo hỏng)
    public EquipmentEntity updateStatus(Long id, EquipmentEntity.Status status) {
        EquipmentEntity equipment = getEquipmentById(id);
        equipment.setStatus(status);
        return equipmentRepository.save(equipment);
    }

    // Lấy danh sách theo trạng thái
    public List<EquipmentEntity> getByStatus(EquipmentEntity.Status status) {
        return equipmentRepository.findByStatus(status);
    }
}
