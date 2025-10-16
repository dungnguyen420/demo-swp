package com.example.swp.controller;

import com.example.swp.DTO.ClassesDTO;
import com.example.swp.DTO.CreateClassBySlotDTO;
import com.example.swp.DTO.CreateClassDTO;
import com.example.swp.DTO.SlotRequest;
import com.example.swp.Entity.ClassesEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserRole;
import com.example.swp.Repository.ClassesRepository;
import com.example.swp.Repository.IUserRepository;
import com.example.swp.Repository.TrainerRepository;
import com.example.swp.Service.IClassesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/classes")
@RequiredArgsConstructor
public class ClassesController {
    private final ClassesRepository repo;

    private final IClassesService classesService;
    private final IUserRepository userRepository;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        CreateClassBySlotDTO dto = new CreateClassBySlotDTO();
        dto.setSlots(new ArrayList<>(List.of(new SlotRequest()))); // 1 hàng mặc định
        model.addAttribute("dto", dto);
        model.addAttribute("trainers", userRepository.findAllByRole(UserRole.TRAINER));
        model.addAttribute("slotNumbers", List.of(1,2,3,4,5,6));
        return "classes/createClass";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("dto") CreateClassBySlotDTO dto, Model model) {
        try {
            ClassesEntity created = classesService.createClassBySlots(dto);
            return "redirect:/classes/" + created.getId();
        } catch (RuntimeException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("trainers", userRepository.findAllByRole(UserRole.TRAINER));
            model.addAttribute("slotNumbers", List.of(1,2,3,4,5,6));
            return "classes/createClass";
        }
    }

    @GetMapping
    public String listClasses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String dir,
            Model model) {

        Page<ClassesEntity> pageData = classesService.listPaged(page, size, sortBy, dir);

        model.addAttribute("pageData", pageData);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("dir", dir);
        return "classes/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        ClassesEntity clazz = repo.findWithAllById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));

        model.addAttribute("clazz", clazz);
        model.addAttribute("trainer", clazz.getTrainer());
        model.addAttribute("schedules", clazz.getSchedules());
        return "classes/detail"; // templates/classes/detail.html
    }

    }


