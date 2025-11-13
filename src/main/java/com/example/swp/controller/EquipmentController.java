package com.example.swp.controller;

import com.example.swp.DTO.EquipmentDTO;
import com.example.swp.Entity.EquipmentEntity;
import com.example.swp.Service.IEquipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/equipment")
public class EquipmentController {

    @Autowired
    private IEquipmentService equipmentService;

    @GetMapping("/list")
    public String listEquipment(Model model){
        List<EquipmentEntity> list = equipmentService.getAllEquipment();
        model.addAttribute("equipments", list);
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
                                @RequestParam(value = "id", required = false) Long id,
                                Model model){
        if (result.hasErrors()){
            model.addAttribute("statuses", EquipmentEntity.Status.values());
            return  "equipment/form";

        }
        EquipmentEntity equipment = equipmentService.saveOrUpdateEquipmentEntity(dto, id);
        return "redirect:/equipment/list";
    }

    @GetMapping("/delete/{id}")
    public String deletEquipment(@PathVariable("id") Long id, Model model){
        try{
            equipmentService.deleteEquipmemt(id);
        }catch (RuntimeException e){
            model.addAttribute("errorMessage", e.getMessage());
            return listEquipment(model);
        }
        return "redirect:/equipment/list";
    }

    @GetMapping("/search")
    public String searchEquipment(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate purchaseDate,
            @RequestParam(required = false) EquipmentEntity.Status status,
            Model model) {

        List<EquipmentEntity> results = equipmentService.searchAdvanced(name, quantity, purchaseDate, status);
        model.addAttribute("equipments", results);
        model.addAttribute("name", name);
        model.addAttribute("quantity", quantity);
        model.addAttribute("purchaseDate", purchaseDate);
        model.addAttribute("status", status);
        model.addAttribute("statuses", EquipmentEntity.Status.values());

        return "equipment/list";
    }
}
