package com.example.swp.controller;

import com.example.swp.DTO.ClassesDTO;
import com.example.swp.Entity.ClassesEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserRole;
import com.example.swp.Repository.TrainerRepository;
import com.example.swp.Service.IClassesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/classes")
@RequiredArgsConstructor
public class ClassesController {

    private final IClassesService classesService;

    private final TrainerRepository trainerRepository;

    // Hiển thị form tạo lớp học
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("classesDTO", new ClassesDTO());
        List<UserEntity> trainers = trainerRepository.findAllTrainerWithProfile(UserRole.TRAINER);
        model.addAttribute("trainers", trainers);
        return "classes/createClass"; // file templates/classes/createClass.html
    }

    // Xử lý submit tạo lớp học (dành cho form, nếu dùng REST đổi thành @PostMapping("/api/create") và trả về JSON)
    @PostMapping("/create")
    public String createClass(@ModelAttribute("classesDTO") ClassesDTO classesDTO, Model model) {
        try {
            ClassesEntity createdClass = classesService.createClasses(classesDTO);
            // Chuyển hướng sang danh sách hoặc trang chi tiết lớp vừa tạo
            return "redirect:/classes/" + createdClass.getId();
        } catch (RuntimeException ex) {
            // Trả lại trang tạo lớp kèm lỗi
            model.addAttribute("errorMessage", ex.getMessage());
            return "classes/createClass";
        }
    }
}
