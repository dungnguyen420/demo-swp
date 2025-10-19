package com.example.swp.controller;

import com.example.swp.DTO.TrainerDTO;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserRole;
import com.example.swp.Service.ITrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/trainers")
public class TrainerController {

    @Autowired
    private ITrainerService trainerService;

    @GetMapping
    public String listTrainers(Model model) {
        List<UserEntity> trainers = trainerService.getAllTrainer(UserRole.TRAINER);
        model.addAttribute("trainers", trainers);
        return "trainer/trainerGrid";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("trainerDTO", new TrainerDTO());
        return "trainer/createTrainer";
    }

    @PostMapping("/create")
    public String createTrainer(@ModelAttribute TrainerDTO trainerDTO) {
        trainerService.createTrainer(trainerDTO);
        return "redirect:/trainers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        TrainerDTO dto = trainerService.trainerDetail(id);
        model.addAttribute("trainerDTO", dto);
        return "trainer/editTrainer";
    }

    @PostMapping("/edit")
    public String updateTrainer(@ModelAttribute TrainerDTO trainerDTO) {
        trainerService.updateTrainer(trainerDTO);
        return "redirect:/trainers";
    }

    @GetMapping("/delete/{id}")
    public String deleteTrainer(@PathVariable Long id) {
        trainerService.deleteTrainer(id);
        return "redirect:/trainers";
    }

    @GetMapping("/detail/{id}")
    public String trainerDetail(@PathVariable Long id, Model model) {
        TrainerDTO dto = trainerService.trainerDetail(id);
        model.addAttribute("trainer", dto);
        return "trainer/trainerDetail";
    }
}

