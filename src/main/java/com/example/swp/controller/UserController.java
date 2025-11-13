package com.example.swp.controller;

import com.example.swp.Entity.UserEntity;
import com.example.swp.Service.IUserService;
import jakarta.persistence.ManyToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model){
        if (userDetails == null){
            return "redirect:/auth/login";
        }
        UserEntity user = userService.findByUserName(userDetails.getUsername());
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/edit")
    public String showEditFrom(@AuthenticationPrincipal UserDetails userDetails, Model model){
        UserEntity user = userService.findByUserName(userDetails.getUsername());
        model.addAttribute("user", user);
        return "user/edit-profile";
    }

    @PostMapping("/edit")
    public String updateProdfile(@AuthenticationPrincipal UserDetails userDetails,
                                 @ModelAttribute("user") UserEntity formUser,
                                 Model model){
        try {
        userService.updateProfile(userDetails.getUsername(), formUser);
        return "redirect:/user/profile?success";
        }catch (RuntimeException e){
            model.addAttribute("user", formUser);
            model.addAttribute("error" , e.getMessage());
            return "user/edit-profile";
        }
    }
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text != null && !text.isEmpty()) {
                    LocalDate localDate = LocalDate.parse(text, dateFormatter);
                    setValue(localDate.atStartOfDay()); // thêm giờ mặc định 00:00
                } else {
                    setValue(null);
                }
            }
        });
    }
}
