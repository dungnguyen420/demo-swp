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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @PostMapping("/edit-profile")
    public String updateProdfile(@AuthenticationPrincipal UserDetails userDetails,
                                 @ModelAttribute("user") UserEntity formUser,
                                 Model model){
        try {
        userService.updateProfile(userDetails.getUsername(), formUser);
        return "redirect:/user/profile?succes";
        }catch (RuntimeException e){
            model.addAttribute("user", formUser);
            model.addAttribute("error" , e.getMessage());
            return "user/edit-profile";
        }
    }
}
