package com.example.swp.controller;

import com.example.swp.DTO.EquipmentDTO;
import com.example.swp.Entity.EquipmentEntity;
import com.example.swp.Service.IEquipmentService;
import com.example.swp.Service.impl.CloudinaryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/equipment")
public class EquipmentController {

    @Autowired
    private IEquipmentService equipmentService;

    @Autowired
    private CloudinaryService cloudinaryService;
    @GetMapping("/list")
    public String listEquipment(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<EquipmentEntity> equipmentPage = equipmentService.getAllEquipmentPaged(pageable);

        model.addAttribute("equipments", equipmentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", equipmentPage.getTotalPages());
        model.addAttribute("totalItems", equipmentPage.getTotalElements());
        model.addAttribute("size", size);

        return "equipment/list";
    }

    @GetMapping("/add")
    public String addForm(Model model){
        model.addAttribute("equipment", new EquipmentDTO());
        model.addAttribute("statuses", EquipmentEntity.Status.values());
        return "equipment/form";
    }

    @GetMapping("edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model){
        EquipmentDTO dto = equipmentService.getEquipmentDTOById(id);
        model.addAttribute("equipment", dto);
        model.addAttribute("statuses", EquipmentEntity.Status.values());
        return "equipment/form";
    }

    @PostMapping("/save")
    public String saveEquipment(@Valid @ModelAttribute("equipment") EquipmentDTO dto,
                                BindingResult result,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                RedirectAttributes redirectAttributes,
                                Model model){
        if (result.hasErrors()){
            model.addAttribute("statuses", EquipmentEntity.Status.values());
            return  "equipment/form";

        }
        try {
            if (imageFile == null || imageFile.isEmpty()) {
                if (dto.getId() != null) {
                    EquipmentEntity old = equipmentService.getEquipmentById(dto.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Thiết bị không tồn tại"));
                    dto.setImage(old.getImage());
                }
            } else {
                String imageUrl = cloudinaryService.uploadFile(imageFile, "equipment");
                dto.setImage(imageUrl);
            }

            boolean isUpdate = dto.getId() != null;
            EquipmentEntity equipment = equipmentService.saveOrUpdateEquipmentEntity(dto, dto.getId());

            if (isUpdate) {
                redirectAttributes.addFlashAttribute("success", "Cập nhật thiết bị thành công!");
            } else {
                redirectAttributes.addFlashAttribute("success", "Tạo thiết bị thành công!");
            }

            return "redirect:/equipment/list";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("statuses", EquipmentEntity.Status.values());
            return "equipment/form";
        }
    }

    @GetMapping("/delete/{id}")
    public String deletEquipment(@PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        try{
            equipmentService.deleteEquipmemt(id);
            redirectAttributes.addFlashAttribute("success", "Xóa thiết bị thành công!");

        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());

        }
        return "redirect:/equipment/list";
    }

    @GetMapping("/search")
    public String searchEquipment(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer quantityMin,
            @RequestParam(required = false) Integer quantityMax,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) EquipmentEntity.Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EquipmentEntity> equipmentPage = equipmentService.searchAdvancedPaged(
                name, quantityMin, quantityMax, startDate, endDate, status, pageable);

        model.addAttribute("equipments", equipmentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", equipmentPage.getTotalPages());
        model.addAttribute("totalItems", equipmentPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("name", name);
        model.addAttribute("quantityMin", quantityMin);
        model.addAttribute("quantityMax", quantityMax);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("status", status);

        return "equipment/list";
    }
}
