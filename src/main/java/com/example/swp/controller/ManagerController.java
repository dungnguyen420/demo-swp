package com.example.swp.controller;

import com.example.swp.DTO.ManagerDTO;
import com.example.swp.DTO.ManagerUpdateDTO;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserGender;
import com.example.swp.Service.IManagerService; // Thay đổi service
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/managers")
public class ManagerController {

    @Autowired
    private IManagerService managerService;

    @GetMapping()
    public String listManagers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UserGender gender,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
        Page<UserEntity> managerPage = managerService.searchManagers(name, gender, pageable);

        model.addAttribute("managerPage", managerPage);
        model.addAttribute("managers", managerPage.getContent());
        model.addAttribute("name", name);
        model.addAttribute("gender", gender);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", managerPage.getTotalPages());
        return "manager/list";
    }


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("managerDTO", new ManagerDTO());
        return "manager/create";
    }

    @PostMapping("/create")
    public String createManager(@Valid @ModelAttribute("managerDTO") ManagerDTO managerDTO,
                                BindingResult br,
                                Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("error", "Dữ liệu nhập không hợp lệ, vui lòng kiểm tra lại.");
            return "manager/create";
        }
        try {
            managerService.createManager(managerDTO);
            ra.addFlashAttribute("message", "Tạo Quản lý mới thành công!");
            return "redirect:/managers";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "manager/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ManagerDTO dto = managerService.managerDetail(id);
        model.addAttribute("managerDTO", dto);
        return "manager/edit";
    }

    @PostMapping("/edit")
    public String updateManager(@Valid @ModelAttribute("managerDTO") ManagerUpdateDTO managerDTO,
                                BindingResult br,
                                Model model, RedirectAttributes ra) {
        if (br.hasErrors()) {
            model.addAttribute("error", "Dữ liệu nhập không hợp lệ, vui lòng kiểm tra lại.");
            model.addAttribute("managerDTO", managerDTO);
            return "manager/edit";
        }
        try {
            managerService.updateManager(managerDTO);
            ra.addFlashAttribute("message", "Cập nhật Quản lý thành công!");
            return "redirect:/managers";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("managerDTO", managerDTO);
            return "manager/edit";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteManager(@PathVariable Long id, RedirectAttributes ra) {
        try {
            managerService.deleteManager(id);
            ra.addFlashAttribute("message", "Đã xóa Quản lý thành công.");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("error", "Xóa thất bại: " + ex.getMessage());
        }
        return "redirect:/managers";

    }

    @GetMapping("/detail/{id}")
    public String managerDetail(@PathVariable Long id, Model model) {
        ManagerDTO dto = managerService.managerDetail(id);
        model.addAttribute("manager", dto);
        return "manager/detail";
    }
}