package com.example.swp.controller;


import com.example.swp.DTO.RegisterDTO;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(){
        return "auth/register";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam("usernameOrEmail") String usernameOrEmail,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        boolean isAuthenticated = userService.authenticateUser(usernameOrEmail, password, session);

        if (!isAuthenticated) {
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không chính xác, hoặc tài khoản bị khóa!");
            return "auth/login";
        }

        UserEntity user = userService.findByUserNameOrEmail(usernameOrEmail);

        if (user.getRole().name().equalsIgnoreCase("ADMIN")) {
            return "redirect:/auth/dashBoard";
        } else {
            return "redirect:/home";
        }
    }
    // 🟨 Xử lý đăng ký
    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegisterDTO dto, Model model) {
        UserEntity newUser = userService.registerUser(dto);

        if (newUser == null) {
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc email đã tồn tại!");
            return "auth/register";
        }

        model.addAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/auth/login";
    }



}
    
