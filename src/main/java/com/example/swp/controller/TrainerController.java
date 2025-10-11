package com.example.swp.controller;

import com.example.swp.DTO.TrainerDTO;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserGender;
import com.example.swp.Enums.UserRole;
import com.example.swp.Service.ITrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/trainers")
public class TrainerController {

    @Autowired
    private ITrainerService trainerService;

    @GetMapping()
    public String listTrainers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UserGender gender,
            @RequestParam(required = false) String specialization,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
        Page<UserEntity> trainerPage = trainerService.search(name, gender, specialization, pageable);

        model.addAttribute("trainerPage", trainerPage); // chú ý: Page<UserEntity>, không phải List<UserEntity>
        model.addAttribute("trainers", trainerPage.getContent());
        model.addAttribute("name", name);
        model.addAttribute("gender", gender);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trainerPage.getTotalPages());
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

