package com.example.swp.controller;

import com.example.swp.Entity.EquipmentEntity;
import com.example.swp.Service.impl.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    // --- CREATE ---
    @PostMapping
    public EquipmentEntity create(@RequestBody EquipmentEntity equipment) {
        return equipmentService.createEquipment(equipment);
    }

    // --- READ ALL ---
    @GetMapping
    public List<EquipmentEntity> getAll() {
        return equipmentService.getAllEquipment();
    }

    // --- READ ONE ---
    @GetMapping("/{id}")
    public EquipmentEntity getById(@PathVariable Long id) {
        return equipmentService.getEquipmentById(id);
    }

    // --- UPDATE FULL INFO ---
    @PutMapping("/{id}")
    public EquipmentEntity update(@PathVariable Long id, @RequestBody EquipmentEntity updated) {
        return equipmentService.updateEquipment(id, updated);
    }

    // --- DELETE ---
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
    }

    // --- UPDATE STATUS ONLY ---
    @PatchMapping("/{id}/status")
    public EquipmentEntity updateStatus(@PathVariable Long id, @RequestParam EquipmentEntity.Status status) {
        return equipmentService.updateStatus(id, status);
    }

    // --- GET BY STATUS ---
    @GetMapping("/status/{status}")
    public List<EquipmentEntity> getByStatus(@PathVariable EquipmentEntity.Status status) {
        return equipmentService.getByStatus(status);
    }
}
