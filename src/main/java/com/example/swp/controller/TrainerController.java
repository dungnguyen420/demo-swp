package com.example.swp.controller;

import com.example.swp.DTO.TrainerDTO;
import com.example.swp.DTO.TrainerUpdateDTO;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserGender;
import com.example.swp.Enums.UserRole;
import com.example.swp.Service.ITrainerService;
import com.example.swp.Service.impl.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        model.addAttribute("trainerPage", trainerPage);
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
    public String createTrainer(@Valid @ModelAttribute("trainerDTO") TrainerDTO trainerDTO,
                                BindingResult br,
                                Model model,RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("error", "Dữ liệu nhập không hợp lệ, vui lòng kiểm tra lại.");
            return "trainer/createTrainer";
        }
        try {
            trainerService.createTrainer(trainerDTO);
            ra.addFlashAttribute("message", "Tạo HLV mới thành công!");
            return "redirect:/trainers";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "trainer/createTrainer";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        TrainerDTO dto = trainerService.trainerDetail(id);
        model.addAttribute("trainerDTO", dto);
        return "trainer/editTrainer";
    }

    @PostMapping("/edit")
    public String updateTrainer(@Valid @ModelAttribute("trainerDTO") TrainerUpdateDTO trainerDTO,
                                BindingResult br,
                                Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("error", "Dữ liệu nhập không hợp lệ, vui lòng kiểm tra lại.");
            return "trainer/editTrainer";
        }
        try {
            trainerService.updateTrainer(trainerDTO);
            ra.addFlashAttribute("message", "Cập nhật HLV thành công!");
            return "redirect:/trainers";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "trainer/editTrainer";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteTrainer(@PathVariable Long id, RedirectAttributes ra) {
        try {
            trainerService.deleteTrainer(id);
            ra.addFlashAttribute("message", "Đã xóa HLV thành công.");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("error", "Xóa thất bại: " + ex.getMessage());
        }
        return "redirect:/trainers";

    }

    @GetMapping("/detail/{id}")
    public String trainerDetail(@PathVariable Long id, Model model) {
        TrainerDTO dto = trainerService.trainerDetail(id);
        model.addAttribute("trainer", dto);
        return "trainer/trainerDetail";
    }


}

